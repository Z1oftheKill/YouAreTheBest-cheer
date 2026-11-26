<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '../api/auth'
import type { LoginResult } from '../types/auth'

defineProps<{
  visible: boolean
}>()

const emit = defineEmits<{
  'update:visible': [val: boolean]
  success: [res: LoginResult]
}>()

const isRegister = ref(false)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  nickname: ''
})

function fillDemo(username: string) {
  isRegister.value = false
  form.username = username
  form.password = '123456'
}

async function handleSubmit() {
  if (!form.username.trim() || !form.password.trim()) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  if (isRegister.value && !form.nickname.trim()) {
    ElMessage.warning('请输入展示昵称')
    return
  }
  loading.value = true
  try {
    const res = isRegister.value
      ? await authApi.register({
          username: form.username.trim(),
          password: form.password,
          nickname: form.nickname.trim()
        })
      : await authApi.login({
          username: form.username.trim(),
          password: form.password
        })
    ElMessage.success(isRegister.value ? '注册并登录成功！' : `欢迎回来，${res.user.nickname}！`)
    emit('success', res)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <el-dialog
    :model-value="visible"
    :title="isRegister ? '注册新账号' : '账号登录'"
    width="420px"
    :close-on-click-modal="false"
    @update:model-value="emit('update:visible', $event)"
  >
    <div v-if="!isRegister" class="demo-tip">
      <span>一键填入测试账号：</span>
      <el-button size="small" type="primary" link @click="fillDemo('cheer')">cheer (1号)</el-button>
      <el-button size="small" type="warning" link @click="fillDemo('guest')">guest (2号)</el-button>
    </div>

    <el-form label-width="76px" @keyup.enter="handleSubmit">
      <el-form-item label="登录账号">
        <el-input v-model="form.username" placeholder="请输入用户名" />
      </el-form-item>

      <el-form-item v-if="isRegister" label="展示昵称">
        <el-input v-model="form.nickname" placeholder="例如：全栈进击者" />
      </el-form-item>

      <el-form-item label="登录密码">
        <el-input v-model="form.password" type="password" show-password placeholder="请输入密码（默认 123456）" />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button link type="primary" @click="isRegister = !isRegister">
          {{ isRegister ? '已有账号？去登录' : '没有账号？注册一个' }}
        </el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          {{ isRegister ? '注册并登录' : '登录' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.demo-tip {
  margin-bottom: 14px;
  padding: 8px 12px;
  border-radius: 6px;
  background: #f8fafc;
  font-size: 12px;
  color: #64748b;
  display: flex;
  align-items: center;
  gap: 6px;
}
.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

