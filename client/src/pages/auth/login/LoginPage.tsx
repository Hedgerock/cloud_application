import {Link} from "react-router-dom";
import '../Auth.css';
import {useConfigureLogin} from "./useConfigureLogin.tsx";
import {memo} from "react";
import {FormTemplate} from "../../../hoc/template/FormTemplate.tsx";
import {ErrorList} from "../../../components/error/ErrorList.tsx";
import type {ErrorResponse} from "../../../hooks/auth/useRegister.ts";

const LoginPage = memo(() => {
    const { handleSubmit, isLoading, isError, Fields, error } = useConfigureLogin();
    
    return (
        <FormTemplate className={"auth-form"} submitFunc={handleSubmit} Fields={Fields}>
            <Link
                className="auth-form__forget-password-link"
                to={"/auth/forgot_password"}
            >
                Forgot password
            </Link>

            <div className="login-buttons">
                <button
                    type="submit"
                    disabled={isLoading}
                    className="login-buttons__button"
                >Login</button>
                <Link
                    to={"/auth/register"}
                    className="login-buttons__button"
                >
                    Register page
                </Link>
            </div>

            { isError && <ErrorList error={error as ErrorResponse} />}
        </FormTemplate>
    )
})

export default LoginPage