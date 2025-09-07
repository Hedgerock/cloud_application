import * as React from "react";
import {type PropsWithChildren} from "react";
import {Header} from "../../components/header/Header.tsx";
import {Footer} from "../../components/footer/Footer.tsx";

export const AppStructure: React.FC<PropsWithChildren> = ({ children }) => {
    return (
        <>
            <Header />
            <main className="main">{ children }</main>
            <Footer />
        </>
    )
}