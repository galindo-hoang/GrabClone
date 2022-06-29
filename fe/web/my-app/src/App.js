import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import UserService from "./service/UserService";

function App() {
    const [name, setName] = useState('');
    useEffect(() => {
        UserService.getUser().then(response => {
            setName(response.data[0].name);
        })
    })
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to reload.
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
                <h1>{name}</h1>
            </header>
        </div>
    );
}

export default App;
