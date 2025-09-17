import '../Auth.css';
import {ErrorList} from "../../../components/error/ErrorList.tsx";
import type {ErrorResponse} from "../../../hooks/auth/useRegister.ts";
import {FormTemplate} from "../../../hoc/template/FormTemplate.tsx";
import {useRestorePassword} from "./useRestorePassword.tsx";

const RestorePasswordPage = () => {
    const {
        isLoading,
        isError,
        error,
        handleSubmit,
        comparePasswords,
        Fields,
        token,
        navigate
    } = useRestorePassword();

    if (!token.length) {
        navigate("/not-found-page");
        return;
    }

    return (
        <>
            <FormTemplate className={"auth-form"} submitFunc={handleSubmit} Fields={Fields}>
                <button type={"submit"} disabled={ !comparePasswords || isLoading }>
                    Confirm new password
                </button>
                { isError && <ErrorList error={error as ErrorResponse} /> }
            </FormTemplate>
        </>
    )
}

export default RestorePasswordPage;