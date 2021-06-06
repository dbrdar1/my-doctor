import { useState } from 'react'
import { useHistory } from 'react-router'
import '../assets/css/registracija.css'
import DugmePrijava from './DugmePrijava'
import { message } from 'antd';
const axios = require('axios').default;


const PromjenaLozinke = () => {
    const history = useHistory();
    let [token, setToken] = useState(false);
    let [info, setInfo] = useState('');
    let [infoSet, setInfoSet] = useState(false);
    let [tokenValue, setTokenValue] = useState('');
    let [lozinka, setLozinka] = useState('');
    let [buttonDisabled, setButtonDisabled] = useState(false);

    const posaljiToken = () => {
        const header = { headers: { "Content-Type": "application/json" } };
        const unos = { userInfo: info };
        axios.post('http://localhost:8080/reset-token', JSON.stringify(unos), header)
            .then((res) => {
                if (res.data.statusniKod === 200) {
                    message.success("Token za resetovanje lozinke Vam je poslan na e-mail.");
                    setInfoSet(true);
                    setButtonDisabled(true);
                }
                else {
                    message.error(res.data.poruka);
                    setButtonDisabled(false);
                }
            });
        setButtonDisabled(true);
    }

    const verifikujToken = () => {
        const header = { headers: { "Content-Type": "application/json" } };
        const unos = { userInfo: info, resetToken: tokenValue };
        axios.post('http://localhost:8080/verifikacijski-podaci', JSON.stringify(unos), header)
            .then((res) => {
                if (res.data.statusniKod === 200) {
                    message.info("Token verificiran. Unesite Vašu novu lozinku.");
                    setToken(true);
                }
                else message.error(res.data.poruka);

            });
    }

    const promijeniLozinku = () => {
        const header = { headers: { "Content-Type": "application/json" } };
        const unos = { userInfo: info, novaLozinka: lozinka };
        axios.put('http://localhost:8080/uredjivanje_lozinke', JSON.stringify(unos), header)
            .then((res) => {
                if (res.data.statusniKod === 200) {
                    message.info("Lozinka promijenjena. Uskoro ćete biti preusmjereni na prijavu.");
                    setTimeout(() => { history.push("/") }, 3300);
                }
            });
    }

    return (
        <div className="blok">
            <div className="opis-prijava opis">
                <h4>Moj doktor</h4>
                <div className="poruka">
                    <br /><br />
                    <h2>Dobro došli!</h2>
                    <p>Mi brinemo o sigurnosti Vašeg računa. Nakon dostavljanja tokena i njegove verifikacije, bit će Vam omogućen unos nove lozinke.</p>
                </div>
            </div>
            <div className="red">
                <h2>Promjena lozinke</h2>
                <input type="text" placeholder="E-mail ili korisničko ime" value={info} onChange={(e) => setInfo(e.target.value)}></input>
                <DugmePrijava disabled={buttonDisabled} klasa={"registracija-registracija"} tekst={"Pošalji token"} onClick={posaljiToken} />
            </div>
            { infoSet &&
                <div className="red">
                    <input type="text" placeholder="Token" value={tokenValue} onChange={(e) => setTokenValue(e.target.value)}></input>
                    <DugmePrijava klasa={"registracija-registracija"} tekst={"Verifikuj token"} onClick={verifikujToken} />
                </div>
            }
            {
                token &&
                <div className="red">
                    <input type="password" placeholder="Nova lozinka" value={lozinka} onChange={(e) => setLozinka(e.target.value)}></input>
                    <DugmePrijava klasa={"registracija-registracija"} tekst={"Promijeni lozinku"} onClick={promijeniLozinku} />
                </div>
            }
        </div>
    );
}

export default PromjenaLozinke;