export const baseApiUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
export const username = 'hedgerock@gmail.com';
export const password = '12345';
export const basicAuth = btoa(`${username}:${password}`);
export const AUTHORIZATION_HEADER = "Authorization";
