import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Registracija from './components/Registracija';
import Prijava from './components/Prijava';
import NaslovnaDoktor from './components/NaslovnaDoktor';
import NaslovnaPacijent from './components/NaslovnaPacijent';
import PregledProfilaDoktor from './components/PregledProfilaDoktor';
import PregledProfilaPacijent from './components/PregledProfilaPacijent';
import UredjivanjeProfilaDoktor from './components/UredjivanjeProfilaDoktor';
import UredjivanjeProfilaPacijent from './components/UredjivanjeProfilaPacijent';
import PregledCVDoktor from './components/PregledCVDoktor';
import PregledCVPacijent from './components/PregledCVPacijent';
import UrediCV from './components/UrediCV';
import DodavanjeTermina from './components/DodavanjeTermina';
import DodavanjePregleda from './components/DodavanjePregleda';
import MojiPacijenti from './components/MojiPacijenti';
import MojiDoktori from './components/MojiDoktori';
import PregledKartonaPacijent from './components/PregledKartonaPacijent';
import PregledKartonaDoktor from './components/PregledKartonaDoktor';
import ChatUIDoktor from './components/ChatUIDoktor';
import ChatUIPacijent from './components/ChatUIPacijent';
import PregledTerminaDoktor from './components/PregledTerminaDoktor';
import PregledTerminaPacijent from './components/PregledTerminaPacijent';
import PromjenaLozinke from './components/PromjenaLozinke';
import { useEffect } from 'react';
import Pregled from './components/Pregled';
import RazgovoriDoktor from './components/RazgovoriDoktor';
import RazgovoriPacijent from './components/RazgovoriPacijent';
import SviDoktori from './components/SviDoktori';
import Stranica404 from './components/stranica404';
import SviPacijenti from './components/SviPacijenti';
import SveNotifikacije from './components/SveNotifikacije';

function App() {
    useEffect(()=>{
        fetch('http://localhost:8080/', { method: "get"}).then(()=>{console.log("Inicijalizirana baza.")});
    }, []);

    return (
        <Router>
            <div className="App" >
                <header className="App-header" >
                    <Switch>
                        <Route exact path="/registracija" component={Registracija} />
                        <Route exact path="/" component={Prijava} />
                        <Route exact path="/doktor/naslovna" component={NaslovnaDoktor} />
                        <Route exact path="/pacijent/naslovna" component={NaslovnaPacijent} />
                        <Route exact path="/doktor/profil" component={PregledProfilaDoktor} />
                        <Route exact path="/pacijent/profil" component={PregledProfilaPacijent} />
                        <Route exact path="/doktor/profil/uredi" component={UredjivanjeProfilaDoktor} />
                        <Route exact path="/pacijent/profil/uredi" component={UredjivanjeProfilaPacijent} />
                        <Route exact path="/doktor/cv" component={PregledCVDoktor} />
                        <Route exact path="/pacijent/cv-doktora/pregled" component={PregledCVPacijent} />
                        <Route exact path="/doktor/cv/uredi" component={UrediCV} />
                        <Route exact path="/termin/dodaj" component={DodavanjeTermina} />
                        <Route exact path="/pregled/dodaj" component={DodavanjePregleda} />
                        <Route exact path="/pacijent/karton/pregled" component={PregledKartonaPacijent} />
                        <Route exact path="/doktor/karton/pregled" component={PregledKartonaDoktor} />
                        <Route exact path="/pregled/:idPregleda" component={Pregled} />
                        <Route exact path="/moji-pacijenti" component={MojiPacijenti} />
                        <Route exact path="/moji-doktori" component={MojiDoktori} />
                        <Route exact path="/doktor/chat/:idPacijentaSagovornika" component={ChatUIDoktor} />
                        <Route exact path="/pacijent/chat/:idDoktoraSagovornika" component={ChatUIPacijent} />
                        <Route exact path="/pacijent/termini" component={PregledTerminaPacijent} />
                        <Route exact path="/doktor/termini" component={PregledTerminaDoktor} />
                        <Route exact path="/promjena-lozinke" component={PromjenaLozinke} />
                        <Route exact path="/doktor/razgovori" component={RazgovoriDoktor} />
                        <Route exact path="/pacijent/razgovori" component={RazgovoriPacijent} />
                        <Route exact path="/pacijent/svi-doktori" component={SviDoktori} />
                        <Route exact path="/doktor/svi-pacijenti" component={SviPacijenti} />
                        <Route exact path="/sve-notifikacije" component={SveNotifikacije} />
                        <Route component={Stranica404} />
                    </Switch>
                </header>
            </div>
        </Router>
    );
}

export default App;