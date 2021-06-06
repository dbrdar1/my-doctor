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

const SviPacijenti = () => {

    const history = useHistory();

    let [loading, setLoading] = useState(true);
    let [pretraga, setPretraga] = useState('');
    let [sviPacijenti, setsviPacijenti] = useState([]);
    const [data, setData] = useState([]);

    const preusmjeriNaChatSaPacijentom = (idPacijentaSagovornika, imePrezimeSagovornika) => {
        history.push({ pathname: "/doktor/chat/" + idPacijentaSagovornika, state: { imePrezimeSagovornika: imePrezimeSagovornika }});
    };

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
        if(localStorage.getItem("uloga")!='DOKTOR') return;
        fetch('http://localhost:8080/korisnici?uloga=PACIJENT', { method: "get" })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                let elementi = [];
                res.forEach(element => {
                    let item = {
                        imePrezime: element.ime + ' ' + element.prezime,
                        key: element.id,
                        akcije: 'Kontaktiraj pacijenta'
                    }
                    elementi.push(item)
                });
                setData(elementi);
                setsviPacijenti(elementi);
                setLoading(false);
            });
    }, []);

    const filtrirajPacijente = () => {
        if (pretraga.length == 0 && data !== sviPacijenti) { setData(sviPacijenti); return; }
        let filtrirani = []
        for (let i = 0; i < sviPacijenti.length; i++)
            if (sviPacijenti[i].imePrezime.toLowerCase().includes(pretraga.toLowerCase())) {
                filtrirani.push(sviPacijenti[i]);
            }
        setData(filtrirani);
    }

    if(localStorage.getItem("uloga")!='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar>
            <HeaderNaslovna stranica="Pacijenti" />
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


export default SviPacijenti;