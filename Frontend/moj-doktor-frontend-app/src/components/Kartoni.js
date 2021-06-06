import React, { useState, useEffect } from 'react';
import { message } from 'antd';
import '../assets/css/karton.css';
import Carousel from 'react-multi-carousel';
import KarticaPregled from './KarticaPregled';
import { useHistory } from 'react-router';
import Loader from './Loader';

const Kartoni = (props) => {
    let [loading, setLoading] = useState(true);

    const responsive = {
        desktop: {
            breakpoint: { max: 3000, min: 1024 },
            items: 3,
            slidesToSlide: 3 // optional, default to 1.
        }
    }

    const history = useHistory();

    const dodajPregledClick = () => {
        history.push({ pathname: "/pregled/dodaj", state: { pacijentPodaci: props.pacijent, stranica: "karton" } });
    }

    const [podaci, setPodaci] = useState({
        imePrezime: '', adresa: '', datumRodjenja: null, brojTelefona: '', email: '',
        spol: '', krvnaGrupa: '', visina: 0, tezina: 0, hronicneBolesti: '', hronicnaTerapija: ''
    });

    const [listaPregleda, setListaPregleda] = useState([])

    const formatirajDatum = (d) => {
        d = new Intl.DateTimeFormat('en-US').format(d);
        let string = d.split('/');
        if (string[0] < 10) string[0] = '0' + string[0]
        if (string[1] < 10) string[1] = '0' + string[1]
        return string[1] + '.' + string[0] + '.' + string[2] + '.';
    }

    const formatirajDatum2 = (datum) => {
        const d = new Date(datum);
        let dan = '' + d.getDate();
        let mjesec = '' + (d.getMonth() + 1);
        if (d.getDate() < 10) dan = '0' + dan;
        if (d.getMonth() < 10) mjesec = '0' + mjesec;
        return dan + '.' + mjesec + '.' + d.getFullYear() + '.';
    }


    const uloga = localStorage.getItem("uloga");

    useEffect(() => {

        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/profil', { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                if (uloga === "PACIJENT") {
                    setPodaci((prev) => {
                        return {
                            ...prev,
                            imePrezime: res.ime + ' ' + res.prezime,
                            korisnickoIme: res.korisnickoIme,
                            adresa: res.adresa,
                            datumRodjenja: formatirajDatum(res.datumRodjenja),
                            brojTelefona: res.brojTelefona,
                            email: res.email
                        }
                    })
                }
                else {
                    setPodaci((prev) => {
                        return {
                            ...prev,
                            imePrezime: props.pacijent.ime + ' ' + props.pacijent.prezime,
                            korisnickoIme: props.pacijent.korisnickoIme,
                            adresa: props.pacijent.adresa,
                            datumRodjenja: formatirajDatum2(props.pacijent.datumRodjenja),
                            brojTelefona: props.pacijent.brojTelefona,
                            email: props.pacijent.email
                        }
                    })
                }
                var kartoniRuta = 'http://localhost:8080/pregledi-kartoni/kartoni-uloga-';
                var ulogaRuta = uloga === "PACIJENT" ? "pacijent/" : "doktor/";
                kartoniRuta = kartoniRuta + ulogaRuta;
                var id = uloga === "PACIJENT" ? res.id : props.pacijent.id;
                var preglediPacijentaURL = 'http://localhost:8080/pregledi-kartoni/pregledi-filtrirano?idPacijent=' + id.toString(10);

                fetch(kartoniRuta + id.toString(10), { method: "get", headers })
                    .then((res2) => {
                        return res2.json();
                    })
                    .then((res2) => {
                        setPodaci((prev) => {
                            return {
                                ...prev,
                                spol: res2.spol,
                                krvnaGrupa: res2.krvnaGrupa,
                                visina: res2.visina,
                                tezina: res2.tezina,
                                hronicneBolesti: res2.hronicneBolesti,
                                hronicnaTerapija: res2.hronicnaTerapija
                            }
                        })
                        setLoading(false);
                    })
                    .catch(() => {
                        message.error("Došlo je do greške prilikom dohvatanja kartona pacijenta!");
                    });

                fetch(preglediPacijentaURL, { method: "get", headers })
                    .then((res2) => {
                        return res2.json();
                    })
                    .then((res2) => {
                        setListaPregleda(res2.reverse())
                        setLoading(false);
                    })
                    .catch(() => {
                        message.error("Došlo je do greške prilikom dohvatanja pregleda pacijenta!");
                    });
            })
            .catch(() => {
                message.error("Došlo je do greške prilikom dohvatanja podataka o profilu trenutnog korisnika!");
            });
    }, []);

    const formatirajZaJSON = (datum) => {
        const d = datum.split('.')
        return d[2] + '-' + d[1] + '-' + d[0]
    }

    const sacuvajPromjene = () => {
        let naziv = podaci.imePrezime.split(' ');
        const unos = {
            ime: naziv[0],
            prezime: naziv[1],
            datumRodjenja: formatirajZaJSON(podaci.datumRodjenja),
            adresa: podaci.adresa,
            brojTelefona: podaci.brojTelefona,
            email: podaci.email,
            spol: podaci.spol,
            krvnaGrupa: podaci.krvnaGrupa,
            visina: podaci.visina,
            tezina: podaci.tezina,
            hronicneBolesti: podaci.hronicneBolesti,
            hronicnaTerapija: podaci.hronicnaTerapija
        }
        const headers = new Headers({
            "Authorization": 'Bearer ' + localStorage.getItem("token"),
            "Content-Type": "application/json"
        });

        fetch('http://localhost:8080/pregledi-kartoni/uredi-karton/' + props.pacijent.id.toString(10), { method: "PUT", body: JSON.stringify(unos), headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                console.log(res)
                if (res.statusniKod !== 200) {
                    message.error(res.poruka);
                }
                else {
                    message.success("Uspješno ste uredili karton.");
                    history.goBack();
                }
            })
            .catch(() => {
                message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });

    }

    return (
        <>
            <div className="licni-podaci">
                {loading ? <><br /><Loader /></> :
                    <>
                        <div className="prvi">
                            <div className="lijevi">
                                <h2 id="ime-prezime">Ime i prezime: {podaci.imePrezime}</h2>
                                <h2 id="datum-rodjenja">Datum rođenja: {podaci.datumRodjenja}</h2>
                            </div>
                            <div className="desni">
                                <h2 id="adresa">Adresa: {podaci.adresa}</h2>
                                <h2 id="broj-telefona">Broj telefona: {podaci.brojTelefona}</h2>
                                <h2 id="mail">E-mail adresa: {podaci.email}</h2>
                            </div>
                            <div className="pocisti" />
                        </div>
                        <div className="drugi">
                            <div className="lijevi">
                                <label htmlFor="spol">Spol: </label>
                                <input id="spol" type="text" disabled={!(localStorage.getItem("uloga") === "DOKTOR")} value={podaci.spol} onChange={(e) => setPodaci((prev) => { return { ...prev, spol: e.target.value } })}></input><br />
                                <label htmlFor="krvna-grupa">Krvna grupa: </label>
                                <input id="krvna-grupa" type="text" disabled={!(localStorage.getItem("uloga") === "DOKTOR")} value={podaci.krvnaGrupa} onChange={(e) => setPodaci((prev) => { return { ...prev, krvnaGrupa: e.target.value } })}></input><br />
                            </div>
                            <div className="desni">
                                <label htmlFor="visina">Visina: </label>
                                <input id="visina" type="text" disabled={!(localStorage.getItem("uloga") === "DOKTOR")} value={podaci.visina} onChange={(e) => setPodaci((prev) => { return { ...prev, visina: e.target.value } })}></input><br />
                                <label htmlFor="tezina">Tezina: </label>
                                <input id="tezina" type="text" disabled={!(localStorage.getItem("uloga") === "DOKTOR")} value={podaci.tezina} onChange={(e) => setPodaci((prev) => { return { ...prev, tezina: e.target.value } })}></input><br />
                            </div>
                        </div>
                        <div className="pocisti" />
                        <div className="prvi">
                            <div className="lijevi">
                                <label htmlFor="hronicne-bolesti">Hronične bolesti: </label>
                                <textarea id="hronicne-bolesti" rows={5} type="text" disabled={!(localStorage.getItem("uloga") === "DOKTOR")} value={podaci.hronicneBolesti} onChange={(e) => setPodaci((prev) => { return { ...prev, hronicneBolesti: e.target.value } })}></textarea><br />
                            </div>
                            <div className="desni">
                                <label htmlFor="hronicna-terapija" >Hronična terapija: </label>
                                <textarea id="hronicna-terapija" rows={5} type="text" disabled={!(localStorage.getItem("uloga") === "DOKTOR")} value={podaci.hronicnaTerapija} onChange={(e) => setPodaci((prev) => { return { ...prev, hronicnaTerapija: e.target.value } })}></textarea><br />
                            </div>
                        </div>
                    </>}
            </div>
            { !loading &&
                <div className="carousel">
                    <Carousel responsive={responsive} >
                        {listaPregleda.map((pregled, index) => (
                            <KarticaPregled key={pregled.id} imePacijenta={podaci.imePrezime.split(' ')[0]} prezimePacijenta={podaci.imePrezime.split(' ')[1]} pregled={pregled} indexPregleda={listaPregleda.length - index} formatirajDatum={formatirajDatum2} />
                        ))}
                    </Carousel>
                    {props.doktor &&
                        <div className="dugmad">
                            <button className="dugme-za-karticu" onClick={dodajPregledClick}>Dodaj pregled</button>
                            <button className="dugme-za-karticu plavo" onClick={sacuvajPromjene}>Spasi</button>
                        </div>
                    }
                </div>
            }
        </>);
}

export default Kartoni;