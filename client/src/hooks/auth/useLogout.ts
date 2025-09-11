import {useLogoutMutation} from "../../redux/api/authApi.ts";
import {useDispatch} from "react-redux";
import type {AppDispatch} from "../../redux/store/store.ts";
import {clearUser, setLoadingStatus} from "../../redux/slices/authSlice.ts";
import {useAuth} from "./useAuth.ts";

export const useLogout = () => {
    const [logout] = useLogoutMutation();
    const { isAuthenticated } = useAuth();
    const dispatch = useDispatch<AppDispatch>();

    const handleLogout = async() => {
        if (!isAuthenticated) return;

        try {
            await logout();
            dispatch(clearUser());
            dispatch(setLoadingStatus({ isLoading: false }))
        } catch (e) {
            console.error("Failed to logout", e);
        }
    }

    return { logout: handleLogout };
}