import React, { useEffect } from 'react';
import { useHistory } from 'react-router';
import Naslovna from './Naslovna';
import Stranica403 from './stranica403';

const NaslovnaPacijent = () => {
    const history = useHistory();
    const podaci = {
        imePrezime: '', korisnickoIme: '', adresa: '', datumRodjenja: null, brojTelefona: '', email: ''
    }

    const formatirajDatum = (d) => {
        d = new Intl.DateTimeFormat('en-US').format(d);
        let string = d.split('/');
        if (string[0] < 10) string[0] = '0' + string[0]
        if (string[1] < 10) string[1] = '0' + string[1]
        return string[2] + '-' + string[0] + '-' + string[1];
    }

    useEffect(() => {
        if(localStorage.getItem("uloga")!=='PACIJENT') return;
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/profil', { method: "get", headers })
            .then((res1) => {
                return res1.json();
            })
            .then((res1) => {
                // doktor - podaci kao korisnik
                podaci.imePrezime = res1.ime + ' ' + res1.prezime;
                podaci.korisnickoIme = res1.korisnickoIme;
                podaci.adresa = res1.adresa;
                podaci.datumRodjenja = formatirajDatum(res1.datumRodjenja);
                podaci.brojTelefona = res1.brojTelefona;
                podaci.email = res1.email;

            })
            .catch(() => {
                //message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });
    }, []);

    const kartice = [
        { naziv: 'Moj profil', opis: 'Moj profil', ikona: 'https://img.icons8.com/cotton/2x/gender-neutral-user--v2.png', onClick: () => { history.push("/pacijent/profil") } },
        { naziv: 'Moji termini', opis: 'Moji termini', ikona: 'https://img.icons8.com/cotton/2x/calendar.png', onClick: () => { history.push("/pacijent/termini") } },
        { naziv: 'Moj karton', opis: 'Moj karton', ikona: 'https://img.icons8.com/cotton/2x/file.png', onClick: () => { history.push("/pacijent/karton/pregled") } },
        { naziv: 'Moji doktori', opis: 'Moji doktori', ikona: 'https://st3.depositphotos.com/24699262/35632/v/950/depositphotos_356326714-stock-illustration-vector-flat-stethoscope-icon-outline.jpg', onClick: () => { history.push("/moji-doktori") }  },
        { naziv: 'Pronađi doktora', opis: 'Pretraga doktora', ikona: 'https://thumbs.dreamstime.com/b/find-doctor-icon-beautiful-meticulously-designed-164650085.jpg', onClick: () => {history.push("/pacijent/svi-doktori")} },
        { naziv: 'Razgovori', opis: 'Moji razgovori', ikona: 'https://img.icons8.com/cotton/2x/chat.png', onClick: () => { history.push("/pacijent/razgovori") }  }
    ];

    if(localStorage.getItem("uloga")!=='PACIJENT') return(<Stranica403/>)
    return (
        <Naslovna kartice={kartice} doktor={false} />
    );
}

export default NaslovnaPacijent;