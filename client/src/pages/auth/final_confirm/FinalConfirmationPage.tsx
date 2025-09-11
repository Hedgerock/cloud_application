import {useConfirm} from "../../../hooks/auth/useConfirm.ts";
import {useEffect} from "react";
import {getLocalStorage, removeFromLocalStorage} from "../../../utils/setAndGetLocalStorage.ts";
import {useLogin} from "../../../hooks/auth/useLogin.ts";
import {useNavigate} from "react-router-dom";
import {AUTH_CRED_KEY, NAVIGATE_TO_REGISTER_PATH, TOKEN_SEARCH_PARAM_KEY} from "../../../utils/constants";
import type {IUserRegisterCredentials} from "../../../models/IUserRegistrationCredentials.ts";

export const FinalConfirmationPage = () => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get(TOKEN_SEARCH_PARAM_KEY) || "";
    const { login } = useLogin();
    const navigate = useNavigate();
    
    const {confirm} = useConfirm();

    useEffect(() => {
        (async () => {

            await confirm(token);
            const credentials = getLocalStorage<IUserRegisterCredentials>(AUTH_CRED_KEY);

            if (!credentials) {
                navigate(NAVIGATE_TO_REGISTER_PATH);
                return;
            }

            try {
                await login(credentials.email, credentials.password);
                removeFromLocalStorage(AUTH_CRED_KEY);
            } catch (e) {
                console.error(e);
                navigate(NAVIGATE_TO_REGISTER_PATH);
            }
        })(); 
    }, [confirm, login, navigate, token]);
    
    return (
        <h1>Confirmed</h1>
    )
}