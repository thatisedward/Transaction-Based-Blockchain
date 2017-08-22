# Transaction-Based-Blockchain

This project is aiming to build a transaction-driven blockchain system under all non-Byzantine conditions including crash, network delays, packet loss and record tampering. This designed blockchain framework belongs to the permissioned blockchain which the nodes added in this system are already liscenced to join in.

I think this project makes the following contributions:
1. The dynasty consensus protocol. The new designed protocol uses the two-phase commit to achieve the consensus result in terms of using two rouds of broadcast of the leader. When designing this protocol, I refered to Raft on the aspect of leader election but improved the message interaction between the current leader and the requesting client. Dynasty protocol ensures the liveness and safety properties under all non-Byzantine conditions.
2. The structure design of the blockchain. In addtion to the consensus model, this blockchain uses three-layer to achieve all functions. The three layers are: the consensus layer, the storage layer and the query layer. The record needed to commit is firstly sent to the consensus layer, if it can be committed after the two rounds of broadcast, then, the record is storaged in the the block structure in the storage layer. At this time, the consensus of committing a record is reached. The query layer is designed to give the record which is requested by the client.

Implementation:
The project has been implemented on the cluster of the shenzhen college of the university of chinese academy of sciences. As shown in the following figures, the test of throughput and tps(transaction per second) has been accomplished on 4, 8, 12, 16 nodes. 

Illustratcion:

![Figure 1. Work folw](https://github.com/thatisedward/Transaction-Based-Blockchain/blob/master/Screenshots/work_flow.JPG)

Figure 1. The interactions in a 4-server cluster with a leader and 3 followers.

![Figure 2. Three-layer design](https://github.com/thatisedward/Transaction-Based-Blockchain/blob/master/Screenshots/three_layer_design.JPG)

Figure 2. The work folw in commmiting a record Ω. 

![Figure 3. Two-step commit](https://github.com/thatisedward/Transaction-Based-Blockchain/blob/master/Screenshots/dy_.JPG)

Figure 3. Normal case operactions.

![Figure 4. View-change](https://github.com/thatisedward/Transaction-Based-Blockchain/blob/master/Screenshots/flow.JPG)

Figure 4. The view-change. The state changed between leader, follower and candidate.
