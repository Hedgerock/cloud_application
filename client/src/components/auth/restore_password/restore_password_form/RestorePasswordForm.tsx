import React, {type FC, useMemo} from "react";
import {useForgotPassword} from "./useForgotPassword.tsx";
import {FormTemplate} from "../../../../hoc/template/FormTemplate.tsx";
import {ErrorList} from "../../../error/ErrorList.tsx";
import type {ErrorResponse} from "../../../../hooks/auth/useRegister.ts";

export const RestorePasswordForm: FC<{ setIsSend: React.Dispatch<boolean> }> = ({ setIsSend }) => {
    const {handleSubmit, Fields, isError, isLoading, error} = useForgotPassword(setIsSend);

    const title = useMemo(() => "Getting back into your Cloud account", []);
    const textContent = useMemo(() => "Tell us some information about your account.", []);

    return (
        <FormTemplate
            className={"auth-form"}
            submitFunc={handleSubmit}
            Fields={Fields}
            title={title}
            textContent={textContent}
        >
            <button type={"submit"} disabled={isLoading}>Send email</button>
            { isError && <ErrorList error={error as ErrorResponse} />}
        </FormTemplate>
    )
}
