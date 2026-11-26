<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { CATEGORY_OPTIONS, PRIORITY_OPTIONS, STATUS_OPTIONS } from '../constants/taskMeta'
import type { CheerTask, TaskSavePayload } from '../types/task'

const props = defineProps<{
  visible: boolean
  editingTask: CheerTask | null
  submitting: boolean
}>()

const emit = defineEmits<{
  'update:visible': [val: boolean]
  submit: [payload: TaskSavePayload]
}>()

const formRef = ref<FormInstance>()

const defaultForm = (): TaskSavePayload => ({
  title: '',
  description: '',
  category: 'STUDY',
  priority: 2,
  status: 0,
  cheerNote: ''
})

const form = reactive<TaskSavePayload>(defaultForm())

const rules: FormRules<TaskSavePayload> = {
  title: [
    { required: true, message: '请输入目标标题', trigger: 'blur' },
    { max: 100, message: '标题不超过 100 个字符', trigger: 'blur' }
  ],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

watch(
  () => props.visible,
  (open) => {
    if (!open) return
    formRef.value?.clearValidate()
    if (props.editingTask) {
      Object.assign(form, {
        title: props.editingTask.title,
        description: props.editingTask.description || '',
        category: props.editingTask.category,
        priority: props.editingTask.priority,
        status: props.editingTask.status,
        cheerNote: props.editingTask.cheerNote || ''
      })
    } else {
      Object.assign(form, defaultForm())
    }
  }
)

async function handleConfirm() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  emit('submit', { ...form })
}
</script>

<template>
  <el-dialog
    :model-value="visible"
    :title="editingTask ? '编辑目标' : '新建成长目标'"
    width="520px"
    @update:model-value="emit('update:visible', $event)"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="88px">
      <el-form-item label="目标标题" prop="title">
        <el-input v-model="form.title" maxlength="100" show-word-limit placeholder="例如：手写一个 Spring Boot 分页接口" />
      </el-form-item>

      <el-form-item label="所属分类" prop="category">
        <el-select v-model="form.category" style="width: 100%">
          <el-option
            v-for="item in CATEGORY_OPTIONS"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="优先级" prop="priority">
        <el-radio-group v-model="form.priority">
          <el-radio-button v-for="item in PRIORITY_OPTIONS" :key="item.value" :value="item.value">
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="当前状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio-button v-for="item in STATUS_OPTIONS" :key="item.value" :value="item.value">
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="详情笔记" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          maxlength="500"
          show-word-limit
          placeholder="记录实现思路或关键步骤（选填）"
        />
      </el-form-item>

      <el-form-item label="打卡感悟" prop="cheerNote">
        <el-input
          v-model="form.cheerNote"
          maxlength="200"
          placeholder="完成后给自己留一句总结或鼓励（选填）"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="emit('update:visible', false)">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleConfirm">保存</el-button>
    </template>
  </el-dialog>
</template>

