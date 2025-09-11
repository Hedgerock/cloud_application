import {useSelector} from "react-redux";
import type {RootState} from "../../redux/store/store.ts";

export const useAuth = () => {
    const { user, isAuthenticated, isLoading } = useSelector((state: RootState) => state.auth);
    return { user, isAuthenticated, isLoading }
}