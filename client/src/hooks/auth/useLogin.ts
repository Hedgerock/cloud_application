import {useLazyGetCurrentUserQuery, useLoginMutation} from "../../redux/api/authApi.ts";
import {useDispatch, useSelector} from "react-redux";
import {setUser} from "../../redux/slices/authSlice.ts";
import type {AppDispatch, RootState} from "../../redux/store/store.ts";
import {clearLoginHistory} from "../../redux/slices/pathSlice.ts";
import {useCallback} from "react";

export const useLogin = () => {
    const [ login, { isLoading, isError, error } ] = useLoginMutation();
    const [ getUser ] = useLazyGetCurrentUserQuery();
    const dispatch = useDispatch<AppDispatch>();
    const health = useSelector((state: RootState) => state.health)

    const handleLogin = useCallback(async(email: string, password: string) => {
        if (!health.isOnline) return;

        try {
            await login({ email, password }).unwrap();
            const user = await getUser().unwrap();
            const roles = user.authorities;

            if (user) dispatch(setUser({ username: user.username, authorities: roles }));

            dispatch(clearLoginHistory());

        } catch (e) {
            console.error("Login failed:", e)
        }
    }, [dispatch, getUser, login, health.isOnline])

    return { login: handleLogin, isLoading, isError, error }
}