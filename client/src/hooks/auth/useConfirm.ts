import {useConfirmMutation} from "../../redux/api/authApi.ts";
import {useNavigate} from "react-router-dom";
import {useCallback} from "react";
import {NAVIGATE_TO_REGISTER_PATH} from "../../utils/constants";

export const useConfirm = () => {
    const [confirm, { isLoading, isError, error }] = useConfirmMutation();
    const navigate = useNavigate();
    
    const handleSuccess = useCallback(async(token: string) => {
        try {
            await confirm({ token: token }).unwrap();
            navigate("/")
        } catch (e) {
            console.error("Failed to confirm", e);
            navigate(NAVIGATE_TO_REGISTER_PATH)
        }
    }, [confirm, navigate])

    return { confirm: handleSuccess, isError, isLoading, error }
}