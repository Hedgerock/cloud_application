import {useLogin} from "../hooks/useLogin.ts";
import * as React from "react";
import {useState} from "react";
import {Link} from "react-router-dom";
import {useSelector} from "react-redux";
import type {RootState} from "../redux/store/store.ts";

export const LoginPage = () => {
    const { login, isLoading, isError } = useLogin();
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const prevPage = useSelector((state: RootState) => state.navigation.prevPath);

    const handleSubmit = async(e: React.FormEvent) => {
        e.preventDefault();
        await login(email, password).then();
    }

    return (
        <>
            <Link to={ prevPage }>Return</Link>
            <form onSubmit={ handleSubmit }>
                <input
                    id="emailInput"
                    name="emailInput"
                    type="email"
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    id="password"
                    name="passwordInput"
                    type="password"
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button type="submit" disabled={isLoading}>Login</button>
                { isError && <h1>Wrong email or password</h1> }
            </form>
        </>
    )
}