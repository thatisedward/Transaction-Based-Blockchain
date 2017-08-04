# Transaction-Based-Blockchain

This project is aiming to build a transaction-driven blockchain system under all non-Byzantine conditions including crash, network delays, packet loss and record tampering. This designed blockchain framework belongs to the permissioned blockchain which the nodes added in this system are already liscenced to join in.

I think this project makes the following contributions:
1. The dynasty consensus protocol. The new designed protocol uses the two-phase commit to achieve the consensus result in terms of using two rouds of broadcast of the leader. When designing this protocol, I refered to Raft on the aspect of leader election but improved the message interaction between the current leader and the requesting client. Dynasty protocol ensures the liveness and safety properties under all non-Byzantine conditions.
2. The structure design of the blockchain. In addtion to the consensus model, this blockchain uses three-layer to achieve all functions. The three layers are: the consensus layer, the storage layer and the query layer. The record needed to commit is firstly sent to the consensus layer, if it can be committed after the two rounds of broadcast, then, the record is storaged in the the block structure in the storage layer. At this time, the consensus of committing a record is reached. The query layer is designed to give the record which is requested by the client.

Implementation:
The project has been implemented on the cluster of the shenzhen college of the university of chinese academy of sciences. As shown in the following figures, the test of throughput and tps(transaction per second) has been accomplished on 4, 8, 12, 16 nodes. 

Result:
The experimental results are not as good as I predicted because of the limitations of the router in the network. 

Analysis:
