import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router';
import '../assets/css/termin.css';
import '../assets/css/karton.css';
import '../assets/css/pretraga.css';
import { Table, Button, Tooltip } from 'antd';
import HeaderNaslovna from './HeaderNaslovna';
import Loader from './Loader';
import { SearchOutlined } from '@ant-design/icons';
import Stranica403 from './stranica403';
import DoktorSideBar from './DoktorSideBar';

const RazgovoriDoktor = () => {

    const history = useHistory();

    let [loading, setLoading] = useState(true);
    let [pretraga, setPretraga] = useState('');
    let [sviPacijenti, setSviPacijenti] = useState([]);
    const [data, setData] = useState([]);

    const preusmjeriNaChatSaPacijentom = (idPacijentaSagovornika, imePrezimeSagovornika) => {
        history.push({ pathname: "/doktor/chat/" + idPacijentaSagovornika, state: { imePrezimeSagovornika: imePrezimeSagovornika }});
    };

    const daLiJeIDUNizu = (id, niz) => {
        for (let i = 0; i < niz.length; i++) {
            if (id === niz[i]) return true;
        }
        return false;
    }

    const [columns, setColumns] = useState([
        {
            title: 'Ime i prezime pacijenta',
            dataIndex: 'imePrezime',
        },
        {
            title: 'Akcije',
            dataIndex: 'akcije',
            render: (tag, record) => <Button type="default" key={tag} onClick={() => { preusmjeriNaChatSaPacijentom(record.key, record.imePrezime); }}>{tag}</Button>
        }
    ]);

    useEffect(() => {
        if (localStorage.getItem("uloga") != 'DOKTOR') return;
        let URL = 'http://localhost:8080/chat/poruke';
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch(URL, { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                let rezultujuciNizPacijenata = [];
                let pronadjeniIDjeviPacijenata = [];
                const fetchedMessages = res;
                fetchedMessages.forEach(fetchedMessage => {
                    if (fetchedMessage.posiljalac.id == localStorage.getItem("id")) {
                        if (!daLiJeIDUNizu(fetchedMessage.primalac.id, pronadjeniIDjeviPacijenata)) {
                            pronadjeniIDjeviPacijenata.push(fetchedMessage.primalac.id);
                            rezultujuciNizPacijenata.push(fetchedMessage.primalac);
                        }
                    }
                    else if (fetchedMessage.primalac.id == localStorage.getItem("id")) {
                        if (!daLiJeIDUNizu(fetchedMessage.posiljalac.id, pronadjeniIDjeviPacijenata)) {
                            pronadjeniIDjeviPacijenata.push(fetchedMessage.posiljalac.id);
                            rezultujuciNizPacijenata.push(fetchedMessage.posiljalac);
                        }
                    }
                });
                let elementi = [];
                rezultujuciNizPacijenata.forEach(pacijent => {
                    let item = {
                        imePrezime: pacijent.ime + ' ' + pacijent.prezime,
                        key: pacijent.id,
                        akcije: 'Nastavi razgovor'
                    }
                    elementi.push(item);
                });
                setData(elementi);
                setSviPacijenti(elementi);
                setLoading(false);
            });
    }, []);

    const filtrirajPacijente = () => {
        if (pretraga.length == 0 && data !== sviPacijenti) { setData(sviPacijenti); return; }
        let filtrirani = [];
        for (let i = 0; i < sviPacijenti.length; i++)
            if (sviPacijenti[i].imePrezime.toLowerCase().includes(pretraga.toLowerCase())) {
                filtrirani.push(sviPacijenti[i]);
            }
        setData(filtrirani);
    }

    if(localStorage.getItem("uloga")!='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar>
            <HeaderNaslovna stranica="Razgovori" />
            <div className="naslovna-sadrzaj">
                <div className="tabela-termina">
                    {loading ? <Loader /> :
                        <>
                            <Tooltip title="Unesite bilo koji dio imena i/ili prezimena pacijenta. Za povratak na sve pacijente, pokrenite pretragu bez unesenog pojma.">
                                <input type="search" className="pretraga" placeholder="Pojam za pretragu" value={pretraga} onChange={(e) => setPretraga(e.target.value)}></input>
                            </Tooltip>
                            <Button shape="circle" icon={<SearchOutlined />} onClick={filtrirajPacijente} />

                            <Table columns={columns} dataSource={data} pagination={{ position: ['None', 'bottomCenter'], pageSize: 5 }} />
                        </>
                    }
                </div>
            </div>
        </DoktorSideBar>
    );
}


export default RazgovoriDoktor;