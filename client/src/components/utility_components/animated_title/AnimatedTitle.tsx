import {type FC, memo} from "react";

export const AnimatedTitle:FC<{ title: string }> = memo(({ title }) => {
    return (
        <>
            <h1 className="animated-title">{ title }</h1>
        </>
    )
})