import Stranica403 from './stranica403';
import React, { useEffect, useState } from 'react';
import '../assets/css/termin.css'
import { Table } from 'antd';
import PacijentSideBar from './PacijentSideBar';
import DoktorSideBar from './DoktorSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import Loader from './Loader';

const SveNotifikacije = () => {
    let [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);
    const [columns, setColumns] = useState([
        {
            title: 'Datum i vrijeme',
            dataIndex: 'datum',
        },
        {
            title: 'Tekst notifikacije',
            dataIndex: 'tekst'
        }
    ]);

    useEffect(() => {
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        if (localStorage.getItem("uloga") !== 'DOKTOR' && localStorage.getItem("uloga") !== 'PACIJENT') return;
        fetch('http://localhost:8080/termini/notifikacije-korisnika/' + localStorage.getItem("id"), { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                console.log(res)
                let elementi = [];
                 res.notifikacije.forEach(element => {
                     let item = {
                        datum: element.datum,
                        tekst: element.tekst
                     }
                     elementi.push(item)
                 });
                setData(elementi.reverse());
                setLoading(false);
            });
    }, [])
    if (localStorage.getItem("uloga") !== 'DOKTOR' && localStorage.getItem("uloga") !== 'PACIJENT') return (<Stranica403 />);
    return (
        <>
            {localStorage.getItem("uloga") === "DOKTOR" ?
                <DoktorSideBar>
                    <HeaderNaslovna stranica="Notifikacije" />
                    <div className="tabela-notifikacija">
                        {loading ? <Loader /> :
                            <Table columns={columns} dataSource={data} pagination={{ position: ['None', 'bottomCenter'], pageSize: 10 }} />
                        }
                    </div>
                </DoktorSideBar>
                :
                <PacijentSideBar>
                    <HeaderNaslovna stranica="Notifikacije" />
                    <div className="tabela-notifikacija">
                        {loading ? <Loader /> :
                            <Table columns={columns} dataSource={data} pagination={{ position: ['None', 'bottomCenter'], pageSize: 10 }} />
                        }
                    </div>
                </PacijentSideBar>
            }
        </>
    );

}

export default SveNotifikacije;
