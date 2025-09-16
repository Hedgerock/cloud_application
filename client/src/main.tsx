import {createRoot} from 'react-dom/client'
import './index.css'
import {App} from "./components/App.tsx";
import {BrowserRouter} from "react-router-dom";
import {Provider} from "react-redux";
import {store} from "./redux/store/store.ts";
import {PathTracker} from "./components/helpers/path_tracker/PathTracker.tsx";

createRoot(document.getElementById('root')!).render(
  <Provider store={store}>
      <BrowserRouter>
          <PathTracker />
          <App />
      </BrowserRouter>
  </Provider>
)