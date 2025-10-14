import axios from 'axios'

const baseURL = '/api'
const lang = localStorage.getItem('language')
Object.assign(axios.defaults, {
  headers: {
    'Content-Type': 'application/json',
    'Cache-Control': 'no-cache',
    Accept: 'application/json',
    'Content-Language': lang === 'zh' ? 'zh_CN' : 'en_US',
  },
  baseURL,
  timeout: 100000,
})
let messageNumber = 0

// const getRefreshToken = async () => {
//   const { user } = store.state
//   if (user.token && user.refreshToken) {
//     const { username, userId, remember, ...tokenBody } = user
//     try {
//       const res = await axios.post('/token', { ...tokenBody })
//       const refreshUser = {
//         username,
//         userId,
//         remember,
//         token: res.data.token,
//         refreshToken: tokenBody.refreshToken,
//       }
//       store.dispatch('UPDATE_USER_INFO', refreshUser)
//       return refreshUser.token
//     } catch (error) {
//       setTimeout(() => {
//         router.push({ path: '/login' })
//       })
//       return Promise.reject(error)
//     }
//   }
//   return false
// }

const handleError = async (error) => {
  const originalRequest = error.config
  const { status, data, config } = error.response
  const isExpired =
    status === 401 &&
    data.indexOf('token is expired by') !== -1 &&
    !originalRequest._retry &&
    process.env.VUE_APP_BUILD_ENV !== 'Fabric'
  if (isExpired) {
    originalRequest._retry = true
    await getRefreshToken()
    return axios(originalRequest)
  }
  const { url } = config
  return Promise.reject(error)
}

axios.interceptors.request.use(
  (config) => {
    config.params = config.params || {}
    return config
  },
  (error) => Promise.reject(error),
)

axios.interceptors.response.use((response) => {
  if (process.env.VUE_APP_BUILD_ENV === 'Fabric' && response.data.code === 16001) {
    window.location = `${window.location.protocol}//${window.location.host}/login?from=kuiper`
    return Promise.reject(response)
  }
  return response
}, handleError)

export default axios
