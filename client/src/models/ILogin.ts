import * as React from "react";

export type InputTypes = "text" | "number" | "password" | "email";

export interface ILoginLabelProps {
    inputType: InputTypes;
    idTitle: string;
    setValue: React.Dispatch<React.SetStateAction<string>>;
    labelTitle: string;
    className?: string;
}
