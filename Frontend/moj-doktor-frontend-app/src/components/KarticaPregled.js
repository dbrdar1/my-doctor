import React, { useEffect, useState } from 'react';
import '../assets/css/kartica-pregled.css';
import '../assets/css/dugme.css'
import { useHistory } from 'react-router';
import { message } from 'antd';
import OcjenaLoader from './OcjenaLoader';

const KarticaPregled = (props) => {
    let [loading2, setLoading2] = useState(true);
    const [termin, setTermin] = useState({ datum: '', vrijeme: '' })

    const history = useHistory();
    const [doktor, setDoktor] = useState({
        ime: '', prezime: ''
    });

    const uloga = localStorage.getItem("uloga");

    useEffect(() => {
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        let URL = uloga === "DOKTOR" ?
            'http://localhost:8080/pregledi-kartoni/doktor-pregleda-uloga-doktor/' + props.pregled.id :
            'http://localhost:8080/pregledi-kartoni/doktor-pregleda-uloga-pacijent/' + props.pregled.id;

        fetch(URL, { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                setDoktor((prev) => {
                    return {
                        ...prev,
                        ime: res.ime,
                        prezime: res.prezime
                    }
                });
                fetch("http://localhost:8080/termini/dobavi-termin/" + props.pregled.idTermina, { method: "get", headers })
                    .then((res) => {
                        return res.json();
                    })
                    .then((res) => {
                        setTermin((prev) => {
                            return {
                                ...prev,
                                datum: props.formatirajDatum(res.datum),
                                vrijeme: res.vrijeme
                            }
                        })
                        setLoading2(false);
                    })
                    .catch(() => {
                        message.error("Došlo je do greške prilikom dohvatanja termina pregleda!");
                    });
            })
            .catch(() => {
                message.error("Došlo je do greške prilikom dohvatanja doktora koji je obavio pregled!");
            });
    }, []);

    return (

        <div className="kartica-pregled">
            {loading2 ? <><br /><br /><OcjenaLoader /></> :
                <>
                    <h1>PREGLED {props.indexPregleda}</h1>
                    <h3 className="prvi-tekst">Datum pregleda: {termin.datum}</h3>
                    <h3>Vrijeme pregleda: {termin.vrijeme}</h3>
                    <h3 className="zadnji-tekst">Pregled obavio: {doktor.ime} {doktor.prezime}</h3>
                    <button className="dugme-za-karticu" onClick={() => history.push({ pathname: "/pregled/" + props.pregled.id, state: { ime: props.imePacijenta, prezime: props.prezimePacijenta, datum: termin.datum, vrijeme: termin.vrijeme } })}>Detalji pregleda</button>
                </>
            }
        </div>
    );
}

export default KarticaPregled;