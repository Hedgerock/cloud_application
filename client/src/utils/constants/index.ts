export const MIN_LENGTH = 6;
export const MAX_LENGTH = 20;
export const AUTH_CRED_KEY = "authCred";
export const NAVIGATE_TO_REGISTER_PATH = "/auth/register";
export const NAVIGATE_TO_LOGIN_PATH = "/auth/register";
export const TOKEN_SEARCH_PARAM_KEY = "token";

export const validatePasswords = (password: string, repeatedPassword: string) => {
    return password.length >= MIN_LENGTH
        && password.length <= MAX_LENGTH
        && repeatedPassword.length >= MIN_LENGTH
        && repeatedPassword.length <= MAX_LENGTH
        && password === repeatedPassword
}