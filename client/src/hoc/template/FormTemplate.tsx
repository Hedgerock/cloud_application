import type {FC, FormEvent, PropsWithChildren, ReactNode} from "react";

interface FormTemplateProps extends PropsWithChildren{
    className: string;
    submitFunc: (e: FormEvent) => void;
    isSubmitButtonIncluded?: boolean;
    Fields: ReactNode;
    title?: string;
    textContent?: string;
    buttonTextContent?: string;
}

export const FormTemplate:FC<FormTemplateProps> = (
    { className, submitFunc, children, Fields,  isSubmitButtonIncluded = true, title, buttonTextContent, textContent}
) => {

    return (
        <form className={className} onSubmit={submitFunc}>
            { title && <h2 className={`${className}__title`}>{ title }</h2> }
            { textContent && <p className={`${className}__text-content`}>{ textContent }</p> }

            <fieldset className={`${className}-fieldset`}>
                { Fields }
            </fieldset>

            { children }

            {!isSubmitButtonIncluded &&
                <button type={"submit"} className={`${className}__submit-button`}>
                    { buttonTextContent ? buttonTextContent : 'Send' }
                </button>
            }
        </form>
    )
}