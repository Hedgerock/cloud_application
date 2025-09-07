import type {FC} from "react";
import type {ILoadingProps} from "../models/ILoadingProps.ts";

export const Loading: FC<ILoadingProps> = ({ children, isLoading }) => {

    return (
        <>
            { !isLoading
                ? <>{ children }</>
                : <h1>Loading...</h1>
            }
        </>
    )
}