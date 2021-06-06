import { message } from 'antd';
import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router';

const UredjivanjeProfila = () => {
    const history = useHistory();
    const [podaci, setPodaci] = useState({
        imePrezime: '', korisnickoIme: '', adresa: '', datumRodjenja: '2000-01-01', brojTelefona: '', email: ''
    });

    const formatirajDatum = (d) => {
        d = new Intl.DateTimeFormat('en-US').format(d);
        let string = d.split('/');
        if (string[0] < 10) string[0] = '0' + string[0]
        if (string[1] < 10) string[1] = '0' + string[1]
        return string[2] + '-' + string[0] + '-' + string[1];
    }
    useEffect(() => {
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/profil', { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
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
            })
            .catch(() => {
                // message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });
    }, []);

    const sacuvajPromjene = () => {
        let string = podaci.imePrezime.split(' ');
        const unos = {
            id: parseInt(localStorage.getItem("id")),
            ime: string[0],
            prezime: string[1],
            datumRodjenja: new Date(podaci.datumRodjenja),
            adresa: podaci.adresa,
            brojTelefona: podaci.brojTelefona,
            email: podaci.email,
            korisnickoIme: podaci.korisnickoIme
        }
        const headers = new Headers({
            "Authorization": 'Bearer ' + localStorage.getItem("token"),
            "Content-Type": "application/json"
        });

        fetch('http://localhost:8080/uredjivanje-profila', { method: "PUT", body: JSON.stringify(unos), headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                console.log(res);
                console.log(JSON.stringify(unos))
                if (res.statusniKod === 200) {
                    message.success("Uspješno ste uredili profil.");
                    history.goBack();
                } else {
                    console.log(JSON.stringify(unos))
                    message.error(res.poruka);
                }
            })
            .catch((err) => {
                console.log("UREDJJ")
                console.log(unos)
                message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });

    }

    return (<div className="kartica-profil">
        <div className="slika-ime">
            <div className="profilna-div">
                <img className="profilna" src="https://hancroft.co.nz/wp-content/uploads/2019/05/profile-placeholder.png" alt="slika profila" />
            </div>
            <div className="imena">
                <label for="ime-prezime">Ime i prezime: </label>
                <input id="ime-prezime" type="text" disabled={false} value={podaci.imePrezime} onChange={(e) => setPodaci((prev) => { return { ...prev, imePrezime: e.target.value } })}></input> <br />
                <label for="korisnicko-ime">Korisničko ime: </label>
                <input id="korisnicko-ime" type="text" disabled={false} value={podaci.korisnickoIme} onChange={(e) => setPodaci((prev) => { return { ...prev, korisnickoIme: e.target.value } })}></input>
                <button className="dugme-promjena-sifre">Promijeni šifru</button>
            </div>
        </div>
        <div className="pocisti" />
        <div className="ostala-polja">
            <label for="datum-rodjenja">Datum rođenja: </label>
            <input id="datum-rodjenja" type="date" disabled={false} onChange={(e) => setPodaci((prev) => { return { ...prev, datumRodjenja: e.target.value } })}></input> <br />
            <label for="adresa">Adresa: </label>
            <input id="adresa" type="text" disabled={false} value={podaci.adresa} onChange={(e) => setPodaci((prev) => { return { ...prev, adresa: e.target.value } })}></input><br />
            <label for="broj-telefona">Broj telefona: </label>
            <input id="broj-telefona" type="text" disabled={false} value={podaci.brojTelefona} onChange={(e) => setPodaci((prev) => { return { ...prev, brojTelefona: e.target.value } })}></input><br />
            <label for="mail">E-mail adresa: </label>
            <input id="mail" type="email" disabled={false} value={podaci.email} onChange={(e) => setPodaci((prev) => { return { ...prev, email: e.target.value } })}></input>
            <button className="dugme-sacuvaj-promjene" onClick={sacuvajPromjene}>Sačuvaj promjene</button>
        </div>
    </div>);
}

export default UredjivanjeProfila;