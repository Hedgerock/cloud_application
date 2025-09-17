import {memo} from "react";
import {Link} from "react-router-dom";
import '../Auth.css';
import {useRegister} from "../../../hooks/auth/useRegister.ts";
import {ErrorList} from "../../../components/error/ErrorList.tsx";
import {FormTemplate} from "../../../hoc/template/FormTemplate.tsx";
import {useGetRegistrationData} from "./useGetRegistrationData.tsx";

const RegistrationPage = memo(() => {
    const {register, isLoading, isError, error} = useRegister();
    const { handleRegister, Fields, isValidPasswords } = useGetRegistrationData(register);

    return (
        <FormTemplate className={"auth-form"} submitFunc={handleRegister} Fields={Fields}>
            <div className="login-buttons">
                <Link
                    to={"/auth/login"}
                    className="login-buttons__button"
                >
                    Login page
                </Link>
                <button
                    type="submit"
                    disabled={!isValidPasswords || isLoading}
                    className="login-buttons__button"
                >Register</button>
            </div>

            { isError && <ErrorList error={error} /> }
        </FormTemplate>
    )
})

export default RegistrationPage