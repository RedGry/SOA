import './App.css';
import {SnackbarProvider} from "notistack";
import {Navigate, Route, Routes} from "react-router-dom";
import FlatsCatalogPage from "./pages/flats-catalog-page";
import {AgencyPage} from "./pages/agency-page";

function App() {
  return (
    <SnackbarProvider maxSnack={3}>
      <Routes>
          <Route path={"/catalog"} element={<FlatsCatalogPage/>}/>
          <Route path={"/agency"} element={<AgencyPage/>}/>
          <Route path={"*"} element={<Navigate to={"/catalog"} replace />}/>
      </Routes>
    </SnackbarProvider>
  );
}

export default App;
