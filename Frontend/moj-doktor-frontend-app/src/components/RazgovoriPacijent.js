import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router';
import '../assets/css/termin.css';
import '../assets/css/karton.css';
import '../assets/css/pretraga.css';
import { Table, Button, Tooltip } from 'antd';
import PacijentSideBar from './PacijentSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import Loader from './Loader';
import { SearchOutlined } from '@ant-design/icons';
import Stranica403 from './stranica403';

const RazgovoriPacijent = () => {

    const history = useHistory();

    let [loading, setLoading] = useState(true);
    let [pretraga, setPretraga] = useState('');
    let [sviDoktori, setSviDoktori] = useState([]);
    const [data, setData] = useState([]);

    const preusmjeriNaChatSaDoktorom = (idDoktoraSagovornika, imePrezimeSagovornika) => {
        history.push({ pathname: "/pacijent/chat/" + idDoktoraSagovornika, state: { imePrezimeSagovornika: imePrezimeSagovornika }});
    };

    const daLiJeIDUNizu = (id, niz) => {
        for (let i = 0; i < niz.length; i++) {
            if (id === niz[i]) return true;
        }
        return false;
    }

    const [columns, setColumns] = useState([
        {
            title: 'Ime i prezime doktora',
            dataIndex: 'imePrezime',
        },
        {
            title: 'Akcije',
            dataIndex: 'akcije',
            render: (tag, record) => <Button type="default" key={tag} onClick={() => { preusmjeriNaChatSaDoktorom(record.key, record.imePrezime); }}>{tag}</Button>
        }
    ]);

    useEffect(() => {
        if (localStorage.getItem("uloga") != 'PACIJENT') return;
        let URL = 'http://localhost:8080/chat/poruke';
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch(URL, { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                let rezultujuciNizDoktora = [];
                let pronadjeniIDjeviDoktora = [];
                const fetchedMessages = res;
                fetchedMessages.forEach(fetchedMessage => {
                    if (fetchedMessage.posiljalac.id == localStorage.getItem("id")) {
                        if (!daLiJeIDUNizu(fetchedMessage.primalac.id, pronadjeniIDjeviDoktora)) {
                            pronadjeniIDjeviDoktora.push(fetchedMessage.primalac.id);
                            rezultujuciNizDoktora.push(fetchedMessage.primalac);
                        }
                    }
                    else if (fetchedMessage.primalac.id == localStorage.getItem("id")) {
                        if (!daLiJeIDUNizu(fetchedMessage.posiljalac.id, pronadjeniIDjeviDoktora)) {
                            pronadjeniIDjeviDoktora.push(fetchedMessage.posiljalac.id);
                            rezultujuciNizDoktora.push(fetchedMessage.posiljalac);
                        }
                    }
                });
                let elementi = [];
                rezultujuciNizDoktora.forEach(doktor => {
                    let item = {
                        imePrezime: doktor.ime + ' ' + doktor.prezime,
                        key: doktor.id,
                        akcije: 'Nastavi razgovor'
                    }
                    elementi.push(item);
                });
                setData(elementi);
                setSviDoktori(elementi);
                setLoading(false);
            });
    }, []);

    const filtrirajDoktore = () => {
        if (pretraga.length == 0 && data !== sviDoktori) { setData(sviDoktori); return; }
        let filtrirani = []
        for (let i = 0; i < sviDoktori.length; i++)
            if (sviDoktori[i].imePrezime.toLowerCase().includes(pretraga.toLowerCase())) {
                filtrirani.push(sviDoktori[i]);
            }
        setData(filtrirani);
    }

    if(localStorage.getItem("uloga")!='PACIJENT') return(<Stranica403/>)
    return (
        <PacijentSideBar>
            <HeaderNaslovna stranica="Razgovori" />
            <div className="naslovna-sadrzaj">
                <div className="tabela-termina">
                    {loading ? <Loader /> :
                        <>
                            <Tooltip title="Unesite bilo koji dio imena i/ili prezimena doktora. Za povratak na sve doktore, pokrenite pretragu bez unesenog pojma.">
                                <input type="search" className="pretraga" placeholder="Pojam za pretragu" value={pretraga} onChange={(e) => setPretraga(e.target.value)}></input>
                            </Tooltip>
                            <Button shape="circle" icon={<SearchOutlined />} onClick={filtrirajDoktore} />

                            <Table columns={columns} dataSource={data} pagination={{ position: ['None', 'bottomCenter'], pageSize: 5 }} />
                        </>
                    }
                </div>
            </div>
        </PacijentSideBar>
    );
}


export default RazgovoriPacijent;