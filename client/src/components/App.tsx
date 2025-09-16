import './App.css'
import 'nprogress/nprogress.css';
import {AppRoutes} from "./routes/AppRoutes.tsx";
import {ConnectionTracker} from "./helpers/connection_tracker/ConnectionTracker.tsx";

export const App = () => {

  return (
      <>
          <ConnectionTracker />
          <AppRoutes />
      </>
  )
}
