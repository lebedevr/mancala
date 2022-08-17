import GameList from "./GameList";
import {useEffect, useState} from "react";

import {backendHost} from './Config';
import stompClient from './StompClient';
import {callStomp} from './StompClient';

const Home = () => {

    const [games, setGames] = useState(null);

    const fetchGames = () => {
        fetch(backendHost + '/games')
            .then(res => {
                return res.json();
            })
            .then(data => {
                setGames(data)
            })
    }

    useEffect(() => {
        fetchGames();
        callStomp(() => stompClient.subscribe('/mancala/updateGameList', function () {
            fetchGames();
        }));
    }, [])

    return (
        <div className="home">
            {games && <GameList games={games}/>}
        </div>
    );
}

export default Home;
