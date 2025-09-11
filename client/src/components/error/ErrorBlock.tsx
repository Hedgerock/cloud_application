import {type FC, memo, useState} from "react";

export const ErrorBlock: FC<{ message: string }> = memo(({ message }) => {
    const [isClosed, setIsClosed] = useState<boolean>(false);
    const handleClosed = () => {
        setIsClosed(true);
    }

    if (isClosed) return null;

    return (
        <div>
            <button onClick={ handleClosed }>Close</button>
            <h1>{ message }</h1>
        </div>
    )
})