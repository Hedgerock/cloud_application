import {type IRegisterCredentials, useRegisterMutation} from "../../redux/api/authApi.ts";
import {useCallback} from "react";
import {useNavigate} from "react-router-dom";
import {setLocalStorage} from "../../utils/setAndGetLocalStorage.ts";
import {AUTH_CRED_KEY} from "../../utils/constants";
import {useSelector} from "react-redux";
import type {RootState} from "../../redux/store/store.ts";

export type ErrorResponse = {
    data: { errors: string[] }
}

export const useRegister = () => {
    const [register, { isLoading, isError, error }] = useRegisterMutation();
    const navigate = useNavigate();
    const health = useSelector((root: RootState) => root.health);

    const handleRegister = useCallback(async({ email, password }: IRegisterCredentials) => {
        if (!health.isOnline) return;
        
        try {
            await register({ email, password }).unwrap();
            navigate("/auth/confirmation_email")
            setLocalStorage<IRegisterCredentials>(AUTH_CRED_KEY, 600_000, { email, password })
        } catch (e) {
            const response = e as ErrorResponse;
            console.log(response.data.errors);
        }
    }, [health.isOnline, register, navigate])

    const getErrors = error as ErrorResponse;

    return { register: handleRegister, isLoading, isError, error: getErrors }
}