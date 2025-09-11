import {useCallback, useEffect, useRef, useState} from "react";

interface IUseGetFocusArgs {
    extraCondition?: boolean;
}

export const useGetFocus = <T extends HTMLElement,>({ extraCondition = true }:IUseGetFocusArgs) => {
    const [isFocused, setIsFocused] = useState<boolean>(false);
    const currentRef = useRef<T>(null);

    const handleFocus = useCallback(() => {
        setIsFocused(prev => !prev && extraCondition);
    }, [extraCondition]);

    const clearFocus = useCallback(() => {
        setIsFocused(false);
    }, []);

    useEffect(() => {
        const handleClickOutside = (event: FocusEvent) => {
            if (currentRef.current && !currentRef.current.contains(event.target as Node) && extraCondition) {
                handleFocus();
            }

            if (currentRef.current && currentRef.current.contains(event.target as Node) && extraCondition) {
                clearFocus();
            }
        }

        const inputRef = currentRef.current;

        inputRef?.addEventListener("focusout", handleClickOutside);
        inputRef?.addEventListener("focusin", handleClickOutside);

        return () => {
            inputRef?.removeEventListener("focusout", handleClickOutside);
            inputRef?.removeEventListener("focusin", handleClickOutside);
        }
    }, [extraCondition, handleFocus, clearFocus]);

    return { currentRef, isFocused, handleFocus }
}