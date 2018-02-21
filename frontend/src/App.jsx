import React from 'react';
import GreetingService from './GreetingService'
import logo from './logo.svg';
import './App.css';

class Greeting extends React.Component {
    render() {
        const {id = null, content = ''} = this.props.message ? this.props.message : {};
        return (
            <p className="App-intro">
                {`${id ? id + ': ' : ''} ${content}`}
            </p>
        );
    }
}

export default class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};

        this.greetingService = new GreetingService();
        this.onclick = this.onclick.bind(this);
    }

    onclick() {
        this.greetingService.greetings(this.input.value)
            .then(message => this.setState({
                message
            }));
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <input type="text" ref={node => this.input = node}/>
                <button onClick={this.onclick}>
                    Say Hi!
                </button>
                <Greeting message={this.state.message}/>
            </div>
        );
    }
}
