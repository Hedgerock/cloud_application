import * as React from "react";

export const LazyStartingPage = React.lazy(() =>
    import("../../pages/starting/StartingPage.tsx"));

export const LazyAuthTemplate = React.lazy(() =>
    import("../../hoc/template/AuthTemplate.tsx"));

export const LazyLoginPage = React.lazy(() =>
    import("../../pages/auth/login/LoginPage.tsx"));

export const LazyConfirmEmailPage = React.lazy(() =>
    import("../../pages/auth/email_confirmation/ConfirmationEmailPage.tsx"));

export const LazyConfirmationPage = React.lazy(() =>
    import("../../pages/auth/final_confirm/FinalConfirmationPage.tsx"));

export const LazyRegistrationPage = React.lazy(() =>
    import("../../pages/auth/registration/RegistrationPage.tsx"));

export const LazyForgotPasswordPage = React.lazy(() =>
    import("../../pages/auth/forgot_password/ForgotPasswordPage.tsx"));

export const LazyNotFoundPage = React.lazy(() =>
    import("../../pages/not_found/NotFoundPage.tsx"));

export const LazyRestorePassword = React.lazy(() =>
    import("../../pages/auth/restore_password/RestorePasswordPage.tsx"));
