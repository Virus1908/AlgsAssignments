import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class BaseballElimination {
    private final ArrayList<String> teams;
    private final int[] wins;
    private final int[] loses;
    private final int[] remaining;
    private final int[][] gamesLeft;
    private final ArrayList<ArrayList<String>> certificates;
    private final Boolean[] isEliminated;

    public BaseballElimination(String filename) {
        In file = new In(filename);
        int size = file.readInt();
        teams = new ArrayList<>();
        wins = new int[size];
        loses = new int[size];
        remaining = new int[size];
        gamesLeft = new int[size][size];
        isEliminated = new Boolean[size];
        certificates = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            teams.add(i, file.readString());
            wins[i] = file.readInt();
            loses[i] = file.readInt();
            remaining[i] = file.readInt();
            for (int j = 0; j < size; j++) {
                gamesLeft[i][j] = file.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return teams.size();
    }

    public Iterable<String> teams() {
        return teams;
    }

    public int wins(String team) {
        return wins[teamIndex(team)];
    }

    private int teamIndex(String team) {
        if (team == null) {
            throw new IllegalArgumentException();
        }
        int indexOf = teams.indexOf(team);
        if (indexOf == -1) {
            throw new IllegalArgumentException();
        }
        return indexOf;
    }

    public int losses(String team) {
        return loses[teamIndex(team)];
    }

    public int remaining(String team) {
        return remaining[teamIndex(team)];
    }


    public int against(String team1, String team2) {
        return gamesLeft[teamIndex(team1)][teamIndex(team2)];
    }


    public boolean isEliminated(String team) {
        int teamIndex = teamIndex(team);
        if (isEliminated[teamIndex] != null) {
            return isEliminated[teamIndex];
        }
        ArrayList<String> certificateSubset = getTeamInTrivialCertificateSubset(teamIndex);
        if (!certificateSubset.isEmpty()) {
            certificates.add(teamIndex, certificateSubset);
            isEliminated[teamIndex] = true;
            return isEliminated[teamIndex];
        }
        FlowNetwork flowNetwork = buildFlowNetworkForEliminatingTeam(teamIndex);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
        int teamsInNetwork = numberOfTeams();
        certificateSubset = new ArrayList<>();
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamIndex) continue;
            if (fordFulkerson.inCut(1 + teamsInNetwork * teamsInNetwork + i)) {
                certificateSubset.add(teams.get(i));
            }
        }
        certificates.add(teamIndex, certificateSubset);
        isEliminated[teamIndex] = !certificateSubset.isEmpty();
        return isEliminated[teamIndex];
    }

    private ArrayList<String> getTeamInTrivialCertificateSubset(int teamIndex) {
        ArrayList<String> certificateSubset = new ArrayList<>();
        int teamMaxWins = wins[teamIndex] + remaining[teamIndex];
        for (int i = 0; i < numberOfTeams(); i++) {
            if (i == teamIndex) continue;
            if (teamMaxWins < wins[i]) {
                certificateSubset.add(teams.get(i));
            }
        }
        return certificateSubset;
    }

    private FlowNetwork buildFlowNetworkForEliminatingTeam(int teamIndex) {
        int teamsInNetwork = numberOfTeams();
        int teamMaxWins = wins[teamIndex] + remaining[teamIndex];
        FlowNetwork flowNetwork = new FlowNetwork(1 + teamsInNetwork * teamsInNetwork + teamsInNetwork + 1);
        for (int i = 0; i < teamsInNetwork; i++) {
            if (teamIndex == i) continue;
            for (int j = i + 1; j < teamsInNetwork; j++) {
                if (teamIndex == j) continue;
                flowNetwork.addEdge(new FlowEdge(0, 1 + i * teamsInNetwork + j, gamesLeft[i][j]));
                flowNetwork.addEdge(new FlowEdge(1 + i * teamsInNetwork + j, 1 + teamsInNetwork * teamsInNetwork + i, gamesLeft[i][j]));
                flowNetwork.addEdge(new FlowEdge(1 + i * teamsInNetwork + j, 1 + teamsInNetwork * teamsInNetwork + j, gamesLeft[i][j]));
            }
            flowNetwork.addEdge(new FlowEdge(1 + teamsInNetwork * teamsInNetwork + i, flowNetwork.V() - 1, teamMaxWins - wins[i]));
        }
        return flowNetwork;
    }


    public Iterable<String> certificateOfElimination(String team) {
        isEliminated[teamIndex(team)] = isEliminated(team);
        ArrayList<String> certificateSubset = certificates.get(teamIndex(team));
        return certificateSubset.isEmpty() ? null : certificateSubset;
    }

}
