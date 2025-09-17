import {useState} from "react";
import '../Auth.css';
import {
    RestorePasswordForm
} from "../../../components/auth/restore_password/restore_password_form/RestorePasswordForm.tsx";
import {SuccessResponse} from "../../../components/auth/restore_password/success_response/SuccessResponse.tsx";

const ForgotPasswordPage = () => {
    const [isSend, setIsSend] = useState<boolean>(false);

    return (
        <>
            {!isSend
                ? <RestorePasswordForm setIsSend={setIsSend} />
                : <SuccessResponse />
            }
        </>
    )
}


export default ForgotPasswordPage;