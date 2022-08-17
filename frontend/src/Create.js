import { useState } from "react";
import { useHistory } from "react-router-dom";
import stompClient from './StompClient';
import { callStomp } from './StompClient';
import { backendHost } from './Config';

const Create = () => {
  const [name, setName] = useState('');
  const [firstPlayer, _] = useState(localStorage.getItem("playerId"));

  const history = useHistory();

  const handleSubmit = (e) => {
    e.preventDefault();
    const game = { name, firstPlayer };

    fetch(backendHost + '/games', {
      method: 'POST',
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(game)
    }).then(res => {
      return res.json();
    }).then((game) => {
      callStomp(() => stompClient.send("/mancala/updateGameList", {}))
      history.push('/games/' + game.id);
    })
  }

  return (
    <div className="create">
      <h2>Add New Game</h2>
      <form onSubmit={handleSubmit}>
        <label>Game name:</label>
        <input 
          type="text" 
          required 
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <button>Add Game</button>
      </form>
    </div>
  );
}
 
export default Create;