import './App.css'
import {Route, Routes} from "react-router-dom";
import {StartingPage} from "../pages/StartingPage.tsx";
import {NotFoundPage} from "../pages/NotFoundPage.tsx";

export const App = () => {

  return (
      <Routes>
          <Route index element={ <StartingPage /> } />
          <Route path="/*" element={<NotFoundPage />}/>
      </Routes>
  )

}
