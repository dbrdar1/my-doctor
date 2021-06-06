import React, { useEffect, useState } from 'react';
import '../assets/css/naslovna-kartica.css'
import '../assets/css/dugme.css'
import { Rate, Modal, message } from 'antd';
import { useHistory } from 'react-router';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import OcjenaLoader from './OcjenaLoader';
const { confirm } = Modal;

const NaslovnaPregledKartica = (props) => {

    const history = useHistory();
    let [ocjena, setOcjena] = useState(0);
    let [ucitavanjeOcjene, setUcitavanjeOcjene] = useState(true)

    const pregledKartonaClick = () => {
        history.push({ pathname: "/doktor/karton/pregled", state: { pacijentPodaci: props.pacijent } });
    }

    const ocijeniDoktora = (novaOcjena) => {
        const poruka = 'Da li ste sigurni da želite ocijeniti doktora ' + props.podaciDoktora.ime + ' ' + props.podaciDoktora.prezime + ' ocjenom ' + novaOcjena + '?';
        confirm({
            title: poruka,
            icon: <ExclamationCircleOutlined />,
            content: 'Nakon klika na OK, doktoru će biti dodana Vaša ocjena.',
            onOk() {
                const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token"), "Content-Type": "application/json" });
                const unos = { id: props.podaciDoktora.id, ocjena: novaOcjena }
                console.log("Unos: ", JSON.stringify(unos))
                fetch('http://localhost:8080/doktor-detalji/ocijeni-doktora/', { method: "PUT", body: JSON.stringify(unos), headers })
                    .then((res) => {
                        return res.json();
                    })
                    .then((res) => {
                        if (res.statusCode === 400) {
                            message.error(res.message);
                            return;
                        }
                        else {
                            console.log("Unos: ", JSON.stringify(unos))
                            console.log("res: ", res)
                            const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
                            fetch('http://localhost:8080/doktor-detalji/doktori/' + props.podaciDoktora.id, { method: "get", headers })
                                .then((res) => {
                                    return res.json();
                                })
                                .then((res) => {
                                    console.log(ocjena)
                                    if (res.ocjena !== 'NaN') setOcjena(res.ocjena)
                                    message.success("Uspješno ste ocijenili doktora.", 2);
                                })
                                .catch(() => {
                                    message.error("Došlo je do greške pri učitavanju podataka. Pokušajte ponovo.");
                                });
                        }
                    });
            },
            onCancel() { },
        });
    }

    useEffect(() => {
        if (localStorage.getItem("uloga") !== "PACIJENT") return;
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/doktor-detalji/doktori/' + props.podaciDoktora.id, { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                if (res.ocjena !== 'NaN') setOcjena(res.ocjena)
                setUcitavanjeOcjene(false);
            })
            .catch(() => {
                message.error("Došlo je do greške pri učitavanju podataka. Pokušajte ponovo.");
            });
    }, [])

    return (
        <div className="kartica-naslovna">
            <img className="profilna" src="https://hancroft.co.nz/wp-content/uploads/2019/05/profile-placeholder.png" alt="slika profila" />
            {!props.doktor ?
                <React.Fragment>
                    <h3>{props.podaciDoktora.ime} {props.podaciDoktora.prezime}</h3>
                    <h3>Adresa: {props.podaciDoktora.adresa}</h3>
                    <h3>Broj telefona: {props.podaciDoktora.brojTelefona}</h3>
                    <div>
                        {ucitavanjeOcjene ?<><OcjenaLoader/></>:
                            <div className="za-doktora">
                                <h3>Ocijeni doktora</h3>
                                <Rate allowHalf value={ocjena} style={{ color: '#001529' }} onChange={novaOcjena => ocijeniDoktora(novaOcjena)} />
                            </div>}
                        <button className="dugme-za-karticu" onClick={() => { history.push({ pathname: "/pacijent/cv-doktora/pregled", state: { idDoktora: props.podaciDoktora.id } }) }}>Pregled CV-a</button>
                    </div>
                </React.Fragment>
                :
                <React.Fragment>
                    <h3>{props.pacijent.ime} {props.pacijent.prezime}</h3>
                    <h3>Adresa: {props.pacijent.adresa}</h3>
                    <h3>Broj telefona: {props.pacijent.brojTelefona}</h3>
                    <button className="dugme-za-karticu" onClick={pregledKartonaClick}>Pregled kartona</button>
                </React.Fragment>
            }
        </div>
    );
}

export default NaslovnaPregledKartica;