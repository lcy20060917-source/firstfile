import { post } from '../utils/request'

export function login(username, password) {
  return post('/auth/login', { username, password })
}

export function register(data) {
  return post('/auth/register', data)
}
