import {useLazyGetCurrentUserQuery, useLoginMutation} from "../redux/api/authApi.ts";
import {useDispatch} from "react-redux";
import {setUser} from "../redux/slices/authSlice.ts";
import type {AppDispatch} from "../redux/store/store.ts";
import {clearLoginHistory} from "../redux/slices/pathSlice.ts";

export const useLogin = () => {
    const [ login, { isLoading, isError, error } ] = useLoginMutation();
    const [ getUser ] = useLazyGetCurrentUserQuery();
    const dispatch = useDispatch<AppDispatch>();

    const handleLogin = async(email: string, password: string) => {
        try {
            await login({ email, password }).unwrap();
            const user = await getUser().unwrap();
            const roles = user.authorities;

            if (user) dispatch(setUser({ username: user.username, authorities: roles }));

            dispatch(clearLoginHistory());

        } catch (e) {
            console.error("Login failed:", e)
        }
    }

    return { login: handleLogin, isLoading, isError, error }
}