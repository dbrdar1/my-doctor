import '../assets/css/loader.css'
import React from 'react';

const Loader = () => {
    return (
        <div className="container-loader"><div className="lds-spinner"><div></div><div></div><div></div><div>
        </div><div></div><div></div><div></div><div></div><div></div><div>
            </div><div></div><div></div></div><br/><br/>Učitavanje sadržaja...</div>
    );
}

export default Loader;