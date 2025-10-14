<template xmlns="">
  <div :class="{'container-login': true, 'left-bg': !darkOrWhite, 'left-bg-dark': darkOrWhite}">
    <!--左侧-->
    <div style="position: absolute;padding-top: 10px;padding-left: 95%" @click="changeBg">
      <div class="sun-moon" >
        <!--  白球    -->
        <div :class="{'white-qiu': true, 'white-qiu-dark': darkOrWhite}">

        </div>
        <!--     太阳   -->
        <img style="width: 20px" src="@/assets/sun.svg">
        <!--     月亮   -->
        <img style="width: 20px" src="@/assets/moon.svg">

      </div>
    </div>

    <div class="container-center">

        <!--   中间左边   -->
        <div class="left-center">
<!--          <div class="left-top">
            <img src="@/assets/logo/logo.png">
            <div class="left-top-text">
              物联网平台
            </div>
          </div>-->
          <div class="left-bottom">
            <img src="@/assets/left-login-img.png">
          </div>
        </div>


        <!--   中间右边   -->
        <div class="right-center">

          <div class="right-top">
            <div :class="{'right-top-text': true, 'right-top-dark-text': darkOrWhite}">
              <svg width="450" height="200" viewBox="0 0 512 224" xmlns="http://www.w3.org/2000/svg">
                <g class="brandname" transform="translate(180, 100) scale(1.2)" style="fill: #1890FF">
                  <svg xmlns="http://www.w3.org/2000/svg" width="185.976" height="55.51200000000001" viewBox="3.6720000000000006 -55.51200000000001 185.976 55.51200000000001"><path d="M14.11 0L14.11 0Q11.30 0 8.93-1.44Q6.55-2.88 5.11-5.26Q3.67-7.63 3.67-10.44L3.67-10.44L3.67-31.32Q3.67-34.13 5.11-36.50Q6.55-38.88 8.93-40.32Q11.30-41.76 14.11-41.76L14.11-41.76L35.57-41.76Q38.45-41.76 40.82-40.32Q43.20-38.88 44.60-36.50Q46.01-34.13 46.01-31.32L46.01-31.32L46.01-15.34L14.69-15.34L14.69-11.02Q14.69-11.02 14.69-11.02Q14.69-11.02 14.69-11.02L14.69-11.02L46.01-11.02L46.01 0L14.11 0ZM14.69-30.74L14.69-24.98L34.99-24.98L34.99-30.74Q34.99-30.74 34.99-30.74Q34.99-30.74 34.99-30.74L34.99-30.74L14.69-30.74Q14.69-30.74 14.69-30.74Q14.69-30.74 14.69-30.74L14.69-30.74ZM62.14 0L62.14 0Q59.33 0 56.95-1.44Q54.58-2.88 53.14-5.26Q51.70-7.63 51.70-10.44L51.70-10.44L51.70-55.51L62.78-55.51L62.78-11.02Q62.78-11.02 62.78-11.02Q62.78-11.02 62.78-11.02L62.78-11.02L71.21-11.02L71.21 0L62.14 0ZM89.14 0L78.12 0L78.12 0L78.12-41.76L89.14-41.76L89.14 0ZM89.14-44.35L78.12-44.35L78.12-44.35L78.12-55.44L89.14-55.44L89.14-44.35ZM108.65 0L97.63 0L97.63 0L97.63-41.76L129.53-41.76Q132.41-41.76 134.78-40.32Q137.16-38.88 138.56-36.50Q139.97-34.13 139.97-31.32L139.97-31.32L139.97 0L128.95 0L128.95-30.74Q128.95-30.74 128.95-30.74Q128.95-30.74 128.95-30.74L128.95-30.74L108.65-30.74Q108.65-30.74 108.65-30.74Q108.65-30.74 108.65-30.74L108.65-30.74L108.65 0ZM158.76 0L147.74 0L147.74 0L147.74-55.44L158.76-55.44L158.76-26.42L164.38-26.42L178.56-41.76L189.65-41.76L189.65-38.02L174.10-20.88L189.65-3.74L189.65 0L178.56 0L164.38-15.34L158.76-15.34L158.76 0Z"></path></svg>
                </g>
                <g class="slogan" transform="translate(260, 180) scale(1.2)" style="fill: #1890FF">
                  <svg xmlns="http://www.w3.org/2000/svg" width="122.41185863398123" height="31.60009381959007" viewBox="7.3980000000000015 -64.69221109407766 122.41185863398123 31.60009381959007"><g transform="scale(0.4444444444444444)" transform-origin="7.3980000000000015 -64.69221109407766"><path paint-order="stroke" stroke-width="0" transform="translate(0, 0)" d="M63.43-62.14L14.54-62.14C11.81-62.14 10.22-60.91 10.15-58.46C10.01-56.02 11.59-54.72 14.40-54.72L26.42-54.72L25.92-47.16C24.41-26.21 20.23-13.10 8.86-1.51C6.77 0.65 6.91 2.81 9.29 4.46C11.45 5.98 13.54 5.83 15.34 3.89C25.70-6.62 30.60-18.65 32.90-34.78L53.42-34.78L50.90-7.63C50.40-2.95 48.89-1.08 45.14-1.08C42.91-1.08 41.18-1.94 39.96-3.67C38.38-5.83 37.08-6.91 34.70-6.48C32.47-6.05 31.32-4.10 32.62-1.37C34.99 3.53 39.38 6.41 45.79 6.41C54.43 6.41 58.39 0.79 59.04-6.48L61.78-35.78C62.14-39.67 59.33-42.41 54.50-42.41L33.77-42.41C33.91-43.92 34.06-45.43 34.13-47.02L34.63-54.72L63.36-54.72C66.10-54.72 67.61-55.94 67.75-58.46C67.90-60.91 66.24-62.14 63.43-62.14Z"></path><path paint-order="stroke" stroke-width="0" transform="translate(72.00000000000001, 0)" d="M8.50-34.99C10.37-34.42 11.81-35.57 12.53-38.30C12.82-39.38 13.10-40.54 13.32-41.62L16.92-41.62L15.91-22.61C13.97-21.96 12.02-21.31 10.01-20.74C7.13-19.94 6.05-18.29 7.06-16.13C8.14-13.90 10.01-13.03 12.53-13.75C13.54-14.04 14.47-14.40 15.48-14.69L14.62 1.73C14.47 4.54 15.77 6.19 18.22 6.19C20.66 6.19 22.03 4.82 22.18 2.02L23.18-17.35C24.62-17.86 26.14-18.43 27.58-19.08C30.24-20.23 31.10-22.10 29.81-24.12C28.51-26.21 26.57-26.71 24.12-25.63C23.98-25.56 23.76-25.49 23.62-25.42L24.48-41.62L27.07-41.62C28.73-41.62 29.66-42.84 29.81-45.29C29.95-47.59 28.30-48.89 25.49-48.89L24.91-48.89L25.49-60.05C25.63-62.86 24.34-64.51 21.96-64.51C19.51-64.51 18.07-63.14 17.93-60.34L17.35-48.89L14.54-48.89C14.76-50.33 14.98-51.84 15.12-53.28C15.34-56.02 14.33-57.60 12.31-57.82C10.30-57.96 9.07-56.52 8.86-53.71C8.50-49.10 7.85-44.50 6.77-39.96C6.12-37.22 6.62-35.57 8.50-34.99ZM31.25-12.60C33.12-11.30 35.06-11.74 36.58-14.11C42.70-23.26 46.37-34.49 48.17-46.66L50.83-46.66C48.53-26.06 43.42-12.31 32.76-0.86C30.67 1.30 30.53 3.24 32.40 4.54C34.42 5.98 36.36 5.76 38.16 3.82C40.25 1.66 42.05-0.72 43.78-3.17C45.65 3.24 49.75 6.41 55.01 6.41C61.49 6.41 64.37 1.73 64.73-5.98L66.89-47.45C67.18-50.69 65.30-53.86 60.26-53.86L41.54-53.86C41.98-55.94 42.41-58.03 42.70-59.98C43.06-62.71 41.90-64.37 39.74-64.66C37.58-64.94 36.29-63.50 35.93-60.70C35.06-54.94 33.55-49.18 30.89-43.42C29.74-40.90 30.02-39.10 31.97-38.02C33.84-37.01 35.71-37.73 36.86-40.32C37.87-42.41 38.74-44.57 39.46-46.66L41.76-46.66C39.89-36.14 36.65-25.99 31.10-18.07C29.45-15.70 29.38-13.90 31.25-12.60ZM45.36-5.54C51.91-16.27 55.44-29.66 57.24-46.66L59.83-46.66L57.60-6.77C57.24-1.22 55.94-0.50 53.57-0.50C52.27-0.50 51.34-1.22 50.62-2.52C49.32-4.82 48.02-5.98 45.43-5.54Z"></path><path paint-order="stroke" stroke-width="0" transform="translate(144.00000000000003, 0)" d="M18.29-32.98L22.32-32.98C20.16-27.07 16.42-22.54 10.58-18.22C7.99-16.27 7.49-14.26 9.50-12.82C11.59-11.30 13.54-11.30 15.55-12.82C19.08-15.48 22.03-18.29 24.41-21.31L30.31-21.31C27.65-14.76 21.67-9.00 13.61-4.10C10.87-2.45 10.22-0.58 11.88 1.08C13.68 2.81 15.70 3.17 17.93 1.80C28.51-4.54 34.99-12.53 37.66-21.31L43.13-21.31C41.69-13.82 39.02-7.63 29.74-1.73C27.07-0.07 26.35 1.73 27.79 3.38C29.38 5.11 31.39 5.47 33.62 4.03C45.50-3.38 48.74-12.38 50.26-21.31L56.52-21.31L55.22-4.54C55.01-1.80 54.29-0.86 52.42-0.86C51.62-0.86 50.98-1.15 50.40-1.66C49.25-2.81 48.17-3.17 46.08-2.38C44.21-1.66 43.49 0.50 45.43 2.81C47.23 4.97 49.68 6.19 53.06 6.19C58.68 6.19 61.85 2.52 62.35-3.96L63.79-22.75C64.08-26.35 60.98-28.15 57.38-28.15L28.51-28.15C29.16-29.66 29.74-31.32 30.17-32.98L55.37-32.98C58.10-32.98 59.62-34.34 59.76-37.15L60.84-56.16C60.98-59.47 59.11-62.21 54.14-62.21L19.94-62.21C17.21-62.21 15.70-60.84 15.55-58.03L14.40-37.44C14.26-34.63 15.70-32.98 18.29-32.98ZM22.32-39.96L22.61-44.35L52.42-44.35L52.20-39.96ZM22.97-50.83L23.18-55.22L53.06-55.22L52.85-50.83Z"></path><path paint-order="stroke" stroke-width="0" transform="translate(216.00000000000006, 0)" d="M24.48 6.34C26.78 6.34 28.08 4.97 28.22 2.16L28.80-9.00C29.74-9.36 30.67-9.65 31.61-10.01C33.48-10.66 34.06-12.17 33.34-14.47C32.62-16.70 31.10-17.57 29.23-16.85L31.25-54.65L32.54-54.65C33.34-54.65 33.91-56.02 34.13-58.46C34.27-60.84 32.76-62.21 29.88-62.21L12.82-62.21C10.08-62.21 8.50-60.91 8.42-58.46C8.35-56.02 9.50-54.65 11.66-54.65L9.36-11.30C8.71-11.16 7.99-10.94 7.34-10.80C5.98-10.51 5.54-9.07 5.98-6.77C6.41-4.39 8.21-3.38 10.94-4.03C14.62-4.90 18.14-5.83 21.67-6.84L21.17 1.87C21.02 4.68 22.25 6.34 24.48 6.34ZM16.42-13.03L16.99-23.98L22.61-23.98L22.10-14.54ZM17.35-30.67L17.78-39.31L23.40-39.31L22.97-30.67ZM18.14-46.01L18.65-54.65L24.26-54.65L23.76-46.01ZM33.26 4.54L33.26 4.54C35.42 5.98 37.51 5.98 39.31 4.03C44.06-1.15 47.16-7.56 49.32-15.12C51.19-7.92 54.50-1.30 59.47 3.60C61.42 5.54 63.50 5.40 65.23 3.74C66.89 2.16 66.53 0.22 64.51-1.80C58.25-7.99 54.58-16.49 53.64-24.84L62.42-24.84C65.16-24.84 66.67-26.06 66.82-28.51C66.96-30.82 65.30-32.11 62.50-32.11L53.21-32.11C53.42-34.06 53.50-36.07 53.64-38.16L53.86-43.20L61.34-43.20C64.08-43.20 65.59-44.50 65.81-46.87C65.95-49.25 64.44-50.47 61.56-50.47L60.70-50.47C62.28-53.06 63.65-55.80 64.80-58.46C65.88-61.20 65.09-63.07 62.93-63.65C60.70-64.22 59.11-63.29 58.10-60.77C56.66-57.31 54.86-53.93 52.63-50.47L39.02-50.47C36.29-50.47 34.78-49.18 34.63-46.87C34.49-44.57 36.07-43.20 38.88-43.20L46.51-43.20L46.37-38.38C46.30-36.22 46.15-34.13 45.94-32.11L37.22-32.11C34.49-32.11 32.98-30.82 32.83-28.51C32.69-26.21 34.27-24.84 37.08-24.84L44.93-24.84C43.06-14.76 39.31-7.06 33.48-1.01C31.39 1.15 31.25 3.10 33.26 4.54ZM38.74-59.04C39.10-57.67 39.60-56.23 40.10-54.94C40.97-52.49 42.70-51.62 44.93-52.06C47.02-52.49 47.45-54.29 46.37-57.17C45.94-58.25 45.58-59.40 45.22-60.48C44.42-63.14 43.13-64.30 41.04-63.94C39.02-63.58 38.02-61.78 38.74-59.04Z"></path></g></svg>
                </g>
                <image class="icon" x="0" y="100" width="120" height="120" href="@/assets/logo/logo.png" filter="url(#color_255_255_255_1733984796507)" style="" color="#ffffff"></image>
              </svg>

            </div>
          </div>



          <el-form ref="loginForm" :model="loginForm" :rules="LoginRules" class="login-form">

            <div v-if="loginForm.loginType === 'uname'">
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
                  <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input  v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
                          @keyup.enter.native="getCode"
                >
                  <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
                </el-input>
              </el-form-item>
<!--              <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">记住密码</el-checkbox>-->
            </div>
            <!-- 下方的登录按钮 -->
            <el-form-item style="width:100%;">
              <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
                         @click.native.prevent="getCode"
              >
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form-item>
          </el-form>
        </div>
    </div>
  </div>
</template>

<script>
import { sendSmsCode, socialAuthRedirect } from '@/api/login'
import { getTenantIdByName } from '@/api/system/tenant'
import { SystemUserSocialTypeEnum } from '@/utils/constants'
import { getCaptchaEnable, getTenantEnable } from '@/utils/ruoyi'
import {
  getPassword,
  getRememberMe, getTenantName,
  getUsername,
  removePassword, removeRememberMe, removeTenantName,
  removeUsername,
  setPassword, setRememberMe, setTenantId, setTenantName,
  setUsername
} from '@/utils/auth'

import Verify from '@/components/Verifition/Verify'
import { resetUserPwd } from '@/api/system/user'

export default {
  name: 'Login',
  components: {
    Verify
  },
  data() {
    return {
      codeUrl: '',
      //默认是明亮主题
      darkOrWhite: false,
      captchaEnable: false,
      tenantEnable: false,
      mobileCodeTimer: 0,
      loginForm: {
        loginType: 'uname',
        username: '',
        password: '',
        captchaVerification: '',
        mobile: '',
        mobileCode: '',
        rememberMe: false,
        tenantName: '芋道源码'
      },
      scene: 21,

      LoginRules: {
        username: [
          { required: true, trigger: 'blur', message: '账号不能为空' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '密码不能为空' }
        ],
        mobile: [
          { required: true, trigger: 'blur', message: '手机号不能为空' },
          {
            validator: function(rule, value, callback) {
              if (/^(?:(?:\+|00)86)?1(?:3[\d]|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8[\d]|9[189])\d{8}$/.test(value) === false) {
                callback(new Error('手机号格式错误'))
              } else {
                callback()
              }
            }, trigger: 'blur'
          }
        ],
        tenantName: [
          { required: true, trigger: 'blur', message: '租户不能为空' },
          {
            validator: (rule, value, callback) => {
              // debugger
              getTenantIdByName(value).then(res => {
                const tenantId = res.data
                if (tenantId && tenantId >= 0) {
                  // 设置租户
                  setTenantId(tenantId)
                  callback()
                } else {
                  callback('租户不存在')
                }
              })
            },
            trigger: 'blur'
          }
        ]
      },
      loading: false,
      redirect: undefined,
      // 枚举
      SysUserSocialTypeEnum: SystemUserSocialTypeEnum
    }
  },
  created() {
    // 租户开关
    this.tenantEnable = getTenantEnable()
    if (this.tenantEnable) {
      getTenantIdByName(this.loginForm.tenantName).then(res => { // 设置租户
        const tenantId = res.data
        if (tenantId && tenantId >= 0) {
          setTenantId(tenantId)
        }
      })
    }
    // 验证码开关
    this.captchaEnable = getCaptchaEnable()
    // 重定向地址
    this.redirect = this.$route.query.redirect ? decodeURIComponent(this.$route.query.redirect) : undefined
    this.getCookie()
  },
  methods: {
    changeBg(){
      this.darkOrWhite = !this.darkOrWhite
    },
    getCode() {
      // 情况一，未开启：则直接登录
      if (!this.captchaEnable) {
        this.handleLogin({})
        return
      }

      // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
      // 弹出验证码
      this.$refs.verify.show()
    },
    getCookie() {
      const username = getUsername()
      const password = getPassword()
      const rememberMe = getRememberMe()
      const tenantName = getTenantName()
      this.loginForm = {
        ...this.loginForm,
        username: username ? username : this.loginForm.username,
        password: password ? password : this.loginForm.password,
        rememberMe: rememberMe ? getRememberMe() : false,
        tenantName: tenantName ? tenantName : this.loginForm.tenantName
      }
    },
    handleLogin(captchaParams) {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          // 设置 Cookie
          if (this.loginForm.rememberMe) {
            setUsername(this.loginForm.username)
            setPassword(this.loginForm.password)
            setRememberMe(this.loginForm.rememberMe)
            setTenantName(this.loginForm.tenantName)
          } else {
            removeUsername()
            removePassword()
            removeRememberMe()
            removeTenantName()
          }
          this.loginForm.captchaVerification = captchaParams.captchaVerification
          // 发起登陆
          // console.log("发起登录", this.loginForm);
          this.$store.dispatch(this.loginForm.loginType === 'sms' ? 'SmsLogin' : 'Login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' }).catch(() => {
            })
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    async doSocialLogin(socialTypeEnum) {
      // 设置登录中
      this.loading = true
      let tenant = false
      if (this.tenantEnable) {
        await this.$prompt('请输入租户名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(async({ value }) => {
          await getTenantIdByName(value).then(res => {
            const tenantId = res.data
            tenant = true
            if (tenantId && tenantId >= 0) {
              setTenantId(tenantId)
            }
          })
        }).catch(() => {
          // 取消登录按钮 loading状态
          this.loading = false

          return false
        })
      } else {
        tenant = true
      }
      if (tenant) {
        // 计算 redirectUri
        const redirectUri = location.origin + '/social-login?'
          + encodeURIComponent('type=' + socialTypeEnum.type + '&redirect=' + (this.redirect || '/')) // 重定向不能丢
        // const redirectUri = 'http://127.0.0.1:48080/api/gitee/callback';
        // const redirectUri = 'http://127.0.0.1:48080/api/dingtalk/callback';
        // 进行跳转
        socialAuthRedirect(socialTypeEnum.type, encodeURIComponent(redirectUri)).then((res) => {
          // console.log(res.url);
          window.location.href = res.data
        })
      }
    },
    /** ========== 以下为升级短信登录 ========== */
    getSmsCode() {
      if (this.mobileCodeTimer > 0) return
      this.$refs.loginForm.validate(valid => {
        if (!valid) return
        sendSmsCode(this.loginForm.mobile, this.scene, this.loginForm.uuid, this.loginForm.code).then(res => {
          this.$modal.msgSuccess('获取验证码成功')
          this.mobileCodeTimer = 60
          let msgTimer = setInterval(() => {
            this.mobileCodeTimer = this.mobileCodeTimer - 1
            if (this.mobileCodeTimer <= 0) {
              clearInterval(msgTimer)
            }
          }, 1000)
        })
      })
    }
  }
}
</script>
<style lang="scss" scoped>
@import "~@/assets/styles/login.scss";

$containerBgImage: '../assets/login-bg.svg';
$containerBgImageDark: '../assets/login-bg-dark.svg';

body{
  margin: 0;
  padding: 0;
}

.white-qiu-dark {
  right: 6px;
  transition: transform .5s,background-color .5s;
  will-change: transform
}

.container-login {
  width: inherit;
  height: inherit;
  min-width: 1080px;
  min-height: 620px;

  .sun-moon{

    position: relative;
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 60px;
    height: 26px;
    padding: 0 6px;
    margin-left: auto;
    cursor: pointer;
    background-color: #151515;
    border-radius: 30px;
    user-select: none;
    .white-qiu {
      position: absolute;
      z-index: 1;
      width: 20px;
      height: 20px;
      background-color: #fff;
      border-radius: 50%;
      transition: transform .5s,background-color .5s;
      will-change: transform
    }
  }

  .container-center {

    flex-direction: row;
    width: 90%;
    height: 100%;


    .left-center {
      position: absolute;
      width: 70%;
      height: 100%;

      .left-top {
        padding-left: 20%;
        display: flex;
        width: 100%;
        font-size: 24px;
        color: #fff;
        line-height: normal;
        img {
          width: 48px;
          height: 48px;
        }
        .left-top-text{
          padding-left: 20px;
          line-height: 48px;
        }
      }

      .left-bottom {
        padding-top: 5%;
        padding-left: 2%;
        width: 100%;
        img{
          width: 90%;
          height: 100%;
        }
      }

    }

    .right-center{

      position: absolute;
      left: 70%;
      top: 20%;
      width: 30%;
      .right-top {
        text-align: center;
        width: 100%;
        font-size: 24px;
        color: #000;
        line-height: normal;
        img {
          width: 150px;
          height: 150px;
        }
        .right-top-text{
          font-weight: bold;
          letter-spacing: 15px;
          padding-left: 20px;
          line-height: 48px;
          padding-bottom: 40px;
        }
        .right-top-dark-text {
          color: #dfe0e3;
        }
      }
      .right-form {

        width: 100%;
        height: 100%;

      }

    }


  }
}

.left-bg::before {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  margin-left: -28%;
  content: '';
  background-image: url($containerBgImage);
  background-repeat: no-repeat;
  background-position: 100%;
  background-size: auto 100%;
}

.left-bg-dark {
  background-color: #293146;
  color: #dfe0e3;
}

.left-bg-dark::before {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  margin-left: -28%;
  content: '';
  background-image: url($containerBgImageDark);
  background-repeat: no-repeat;
  background-position: 100%;
  background-size: auto 100%;
}

.oauth-login {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.oauth-login-item {
  display: flex;
  align-items: center;
  margin-right: 10px;
}

.oauth-login-item img {
  height: 25px;
  width: 25px;
}

.oauth-login-item span:hover {
  text-decoration: underline red;
  color: red;
}

.sms-login-mobile-code-prefix {
  :deep(.el-input__prefix) {
    top: 22%;
  }
}

.login-form {
  width: 50%;
  margin-left: 21%;
}
</style>
