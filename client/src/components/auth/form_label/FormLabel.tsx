import {type FC, memo} from "react";
import type {ILoginLabelProps} from "../../../models/ILogin.ts";
import {useGetLabelCredentials} from "../../../pages/auth/login/useGetLabelCredentials.ts";
import './FormLabel.css';
import {useGetFocus} from "../../../hooks/useGetFocus.ts";

export const FormLabel: FC<ILoginLabelProps> = memo(
    ({ inputType, setValue, idTitle, labelTitle, className = "auth-form" }) => {
    const { textContent, type, inputTitle, handleType, reset, handleChangeValue } =
        useGetLabelCredentials({ inputType, setValue, idTitle });

    const {
        currentRef,
        isFocused,
        handleFocus
    } = useGetFocus<HTMLLabelElement>({ extraCondition: inputType === "password" });

    return (
        <label className={`${className}-fieldset-label`} ref = {currentRef}>
            <span className={`${className}-fieldset-label__title`}>{ labelTitle }</span>
            <input
                id={`form${inputTitle}`}
                name={`form${inputTitle}Input`}
                type={ type }
                className={`${className}-fieldset-label__value`}
                onChange={ handleChangeValue }
                onFocus={handleFocus}
            />

            { inputType === "password" && isFocused &&
                <button
                    tabIndex={-1}
                    className={`${className}-fieldset-label__show-hide-button`}
                    type="button"
                    onMouseDown={handleType}
                    onMouseUp={reset}
                    onMouseLeave={reset}
                >
                    { textContent }
                </button>
            }
        </label>
    )
})