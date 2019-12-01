from rlbottraining.common_exercises.kickoff_exercise import KickoffExercise, Spawns
from pathlib import Path
from rlbot.matchconfig.match_config import PlayerConfig, Team

def make_default_playlist():
    exercises = [
        KickoffExercise('Left Corner', blue_spawns=[Spawns.CORNER_L], orange_spawns = [])
    ]
   
    for ex in exercises:
        ex.match_config.player_configs = [
            PlayerConfig.bot_config(
                Path(__file__).absolute().parent.parent / 'src' / 'main' / 'python' / 'javaExample.cfg',
                Team.BLUE
            )
        ]
    
    return exercises