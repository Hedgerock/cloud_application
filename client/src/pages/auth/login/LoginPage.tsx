import {Link} from "react-router-dom";
import '../Auth.css';
import {useConfigureLogin} from "./useConfigureLogin.ts";
import {ErrorBlock} from "../../../components/error/ErrorBlock.tsx";
import {FormLabel} from "../../../components/auth/form_label/FormLabel.tsx";
import {memo} from "react";

const LoginPage = memo(() => {
    const { handleSubmit, setEmail, setPassword, isLoading, isError } = useConfigureLogin();
    
    return (
        <form onSubmit={ handleSubmit } className="auth-form">
            <fieldset className="auth-form-fieldset">
                <FormLabel
                    labelTitle={"Login"}
                    inputType={"email"}
                    idTitle={"authEmail"}
                    setValue={setEmail}
                />

                <FormLabel
                    labelTitle={"Password"}
                    inputType={"password"}
                    idTitle={"authPassword"}
                    setValue={setPassword}
                />
            </fieldset>

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

            { isError && <ErrorBlock message={"Wrong email or password"}/> }
        </form>
    )
})

export default LoginPage