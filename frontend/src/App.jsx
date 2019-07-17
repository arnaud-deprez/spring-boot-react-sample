import React, {useState, useRef} from 'react';
import GreetingService from './GreetingService'
import logo from './logo.svg';
import './App.css';

const Header = () => {
    return (
        <header className="app-header">
            <img src={logo} className="app-logo" alt="logo"/>
            <h1 className="app-title">Welcome to React</h1>
        </header>);
};

const GreetingMessage = ({message = {}}) => {
    const {id = null, content = ''} = message;
    return (
        <p className="app-intro">
            {`${id ? id + ': ' : ''} ${content}`}
        </p>
    );
};

const Greeting = ({onClick}) => {
    const inputEl = useRef("");
    return (
        <div>
            <input type="text" ref={inputEl}/>
            <button onClick={() => onClick(inputEl.current.value)}>
                Say Hi!
            </button>
        </div>
    );
};

const App = () => {
    const [message, setMessage] = useState("");
    const greetingService = new GreetingService();

    const onClick = value =>
        greetingService.greetings(value)
            .then(setMessage);

    return (
        <div className="app">
            <Header/>
            <Greeting onClick={onClick}/>
            <GreetingMessage message={message}/>
        </div>
    );
};

export default App;
