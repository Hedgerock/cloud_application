import {Navigate, Route, Routes} from "react-router-dom";
import {StartingPage} from "../../pages/starting/StartingPage.tsx";
import {NonAuthBoundary} from "../../hoc/boundaries/non_auth_boundary/NonAuthBoundary.tsx";
import {AuthTemplate} from "../../hoc/template/AuthTemplate.tsx";
import {LoginPage} from "../../pages/auth/login/LoginPage.tsx";
import {RegistrationPage} from "../../pages/auth/registration/RegistrationPage.tsx";
import {NotFoundPage} from "../../pages/not_found/NotFoundPage.tsx";
import {ConfirmationEmailPage} from "../../pages/auth/emailConfiramtion/ConfirmationEmailPage.tsx";
import {FinalConfirmationPage} from "../../pages/auth/final_confirm/FinalConfirmationPage.tsx";

export const AppRoutes = () => {

    return (
        <Routes>
            <Route index element={<StartingPage />} />

            <Route path="auth" element={
                <NonAuthBoundary>
                    <AuthTemplate />
                </NonAuthBoundary>
            }
            >
                <Route path={"login"} element={<LoginPage />} />
                <Route path={"register"} element={<RegistrationPage />} />
                <Route path={"confirmation_email"} element={<ConfirmationEmailPage />} />
                <Route path={"confirm"} element={<FinalConfirmationPage />} />
            </Route>

            <Route path="/*" element={
                <Navigate to={"/not-found-page"} replace={true}/>
            }
            />
            <Route path="/not-found-page" element={ <NotFoundPage /> } />
        </Routes>
    )
}