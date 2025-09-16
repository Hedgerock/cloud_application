import {useConfirmMutation} from "../../redux/api/authApi.ts";
import {useNavigate} from "react-router-dom";
import {useCallback} from "react";
import {NAVIGATE_TO_REGISTER_PATH} from "../../utils/constants";
import {useSelector} from "react-redux";
import type {RootState} from "../../redux/store/store.ts";

export const useConfirm = () => {
    const [confirm, { isLoading, isError, error }] = useConfirmMutation();
    const navigate = useNavigate();
    const health = useSelector((root: RootState)=> root.health);
    
    const handleSuccess = useCallback(async(token: string) => {
        if (!health.isOnline) return;
        
        try {
            await confirm({ token: token }).unwrap();
            navigate("/")
        } catch (e) {
            console.error("Failed to confirm", e);
            navigate(NAVIGATE_TO_REGISTER_PATH)
        }
    }, [confirm, health.isOnline, navigate])

    return { confirm: handleSuccess, isError, isLoading, error }
}